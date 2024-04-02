package fail.tiger.jellytiger.utils.jellyfin

import fail.tiger.jellytiger.JellyTigerApplication.Companion.jellyTiger
import fail.tiger.jellytiger.JellyTigerApplication.Companion.mmkv
import fail.tiger.jellytiger.R
import fail.tiger.jellytiger.data.db.Server
import fail.tiger.jellytiger.data.db.User
import fail.tiger.jellytiger.utils.strResource
import fail.tiger.jellytiger.utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jellyfin.sdk.api.client.extensions.systemApi
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.discovery.RecommendedServerInfo
import org.jellyfin.sdk.discovery.RecommendedServerInfoScore
import org.jellyfin.sdk.discovery.RecommendedServerIssue
import org.jellyfin.sdk.model.api.AuthenticateUserByName
import org.jellyfin.sdk.model.api.PublicSystemInfo

/**
 * Inspired by: https://github.com/jarnedemeulemeester/findroid/core/src/main/java/dev/jdtech/jellyfin/viewmodels/AddServerViewModel.kt
 */
suspend fun getRecommendedServer(baseUrl: String): RecommendedServerInfo {
    val recommendedServersList =
        jellyTiger.jellyfin.discovery.getRecommendedServers(jellyTiger.jellyfin.discovery.getAddressCandidates(baseUrl))
    val greatServers =
        recommendedServersList.filter { it.score == RecommendedServerInfoScore.GREAT }
    if (greatServers.isNotEmpty()) return greatServers.first()

    val goodServers = recommendedServersList.filter { it.score == RecommendedServerInfoScore.GOOD }
    if (goodServers.isNotEmpty()) return goodServers.first()

    val okServers = recommendedServersList.filter { it.score == RecommendedServerInfoScore.OK }
    if (okServers.isNotEmpty()) return okServers.first()

    val badServers = recommendedServersList.filter { it.score == RecommendedServerInfoScore.BAD }
    if (badServers.isNotEmpty() && !badServers.first().issues.isEmpty()) {
        when (badServers.first().issues.first()) {
//            is RecommendedServerIssue.SecureConnectionFailed-> throw Exception(strResource(R.string.error_secure_connection_failed))
//            is RecommendedServerIssue.ServerUnreachable-> throw Exception(strResource(R.string.error_server_unreachable))
            is RecommendedServerIssue.MissingSystemInfo -> throw Exception(strResource(R.string.error_missing_system_info))
            is RecommendedServerIssue.InvalidProductName -> throw Exception(strResource(R.string.error_invalid_product_name))
            is RecommendedServerIssue.MissingVersion -> throw Exception(strResource(R.string.error_missing_version))
            is RecommendedServerIssue.UnsupportedServerVersion -> throw Exception(strResource(R.string.error_unsupported_server_version))
            is RecommendedServerIssue.OutdatedServerVersion -> throw Exception(strResource(R.string.error_outdated_server_version))
            is RecommendedServerIssue.SlowResponse -> throw Exception(strResource(R.string.error_slow_response))
        }
    }
    throw Exception(strResource(R.string.error_unknown))
}

suspend fun getPublicSystemInfo(baseUrl: String, then: (PublicSystemInfo) -> Unit) {
    try {
        val systemInfo = getRecommendedServer(baseUrl).systemInfo.getOrNull()
        then(systemInfo!!)
    } catch (e: Exception) {
        toast(e.message.toString())
    }
}

suspend fun loginByUserName(baseUrl: String, username: String, pw: String, then: () -> Unit) {
    jellyTiger.jfApi = jellyTiger.jellyfin.createApi(baseUrl = baseUrl)
    val api = jellyTiger.jfApi!!
    try {
        val authenticationResult by api.userApi.authenticateUserByName(
            AuthenticateUserByName(username = username, pw = pw)
        )
        api.accessToken = authenticationResult.accessToken
        api.userId = authenticationResult.user!!.id
        jellyTiger.globalServer = Server(
            api.systemApi.getSystemInfo().content.id!!,
            api.systemApi.getSystemInfo().content.serverName,
            api.baseUrl, null, null,
            api.systemApi.getSystemInfo().content.version,
        )
        jellyTiger.globalUser = User(
            authenticationResult.user!!.id.toString(),
            api.systemApi.getSystemInfo().content.id!!,
            authenticationResult.accessToken,
            authenticationResult.user!!.name,
            authenticationResult.user!!.primaryImageTag,
            authenticationResult.user!!.policy!!.isAdministrator,
            authenticationResult.user!!.policy!!.isDisabled,
            true
        )
        withContext(Dispatchers.IO) {
            jellyTiger.dbServer.insert(jellyTiger.globalServer!!)
            jellyTiger.dbUser.insert(jellyTiger.globalUser!!)
        }
        mmkv.encode("nowServer", jellyTiger.globalServer!!.serverId)
        mmkv.encode("nowUser", jellyTiger.globalUser!!.userId)
        then()
    } catch (e: Exception) {
        toast(e.message.toString())
    }
}