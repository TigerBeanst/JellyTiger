package fail.tiger.jellytiger.data

import fail.tiger.jellytiger.data.db.Server
import fail.tiger.jellytiger.data.db.ServerDao
import fail.tiger.jellytiger.data.db.User
import fail.tiger.jellytiger.data.db.UserDao
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.model.api.UserDto

data class JellyTiger(
    var jellyfin: Jellyfin,
    var dbServer: ServerDao,
    var dbUser: UserDao,
    var jfApi: ApiClient? = null,
    var globalUser: User? = null,
    var globalServer: Server? = null,
    var globalUserData: UserDto? = null
)