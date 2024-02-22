package fail.tiger.jellytiger.utils

import fail.tiger.jellytiger.JellyTigerApplication.Companion.appContext

fun strResource(id: Int): String {
    return appContext.getString(id)
}