package fail.tiger.jellytiger.utils

import android.util.Log
import android.view.View
import android.widget.Toast
import fail.tiger.jellytiger.JellyTigerApplication.Companion.appContext


fun logd(message: String) =
    Log.d("JellyTiger", message)

fun toast(message: Any, isStringResId: Boolean = false) =
    if (isStringResId) {
        Toast.makeText(appContext, appContext.getString(message as Int), Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(appContext, message.toString(), Toast.LENGTH_SHORT).show()
    }

fun longtoast(message: CharSequence) =
    Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()