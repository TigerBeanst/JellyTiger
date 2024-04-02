import fail.tiger.jellytiger.JellyTigerApplication.Companion.jellyTiger
import fail.tiger.jellytiger.utils.logd

/**
 * Get if the user is logged in, this function will not check the user's token validity
 */
fun getLoginStatus():Boolean{
    val user = jellyTiger.dbUser.getCurrentUser()
    if(user!=null){
        logd("Current User: ${user.userName}")
        return true
    }else{
        logd("No User Available, Please Login First")
        return false
    }
}