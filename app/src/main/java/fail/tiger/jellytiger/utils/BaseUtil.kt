package fail.tiger.jellytiger.utils


import android.content.Intent
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import fail.tiger.jellytiger.JellyTigerApplication.Companion.appContext
import java.io.Serializable

fun Any?.startActivityEnhanced(targetActivity: Class<*>, vararg extras: Pair<String, Any?>) {
    val context = when (this) {
        is ComponentActivity -> this
        is Fragment -> this.context
        else -> appContext
    }
    val intent = Intent(context, targetActivity)
    if (context == appContext) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    extras.forEach { (extraKey, extraValue) ->
        when (extraValue) {
            is Int -> intent.putExtra(extraKey, extraValue)
            is String -> intent.putExtra(extraKey, extraValue)
            is Boolean -> intent.putExtra(extraKey, extraValue)
            is Float -> intent.putExtra(extraKey, extraValue)
            is Double -> intent.putExtra(extraKey, extraValue)
            is Long -> intent.putExtra(extraKey, extraValue)
            is Char -> intent.putExtra(extraKey, extraValue)
            is Short -> intent.putExtra(extraKey, extraValue)
            is Byte -> intent.putExtra(extraKey, extraValue)
            is CharSequence -> intent.putExtra(extraKey, extraValue)
            is ArrayList<*> -> intent.putExtra(extraKey, extraValue)
            is Array<*> -> intent.putExtra(extraKey, extraValue)
            is IntArray -> intent.putExtra(extraKey, extraValue)
            is FloatArray -> intent.putExtra(extraKey, extraValue)
            is DoubleArray -> intent.putExtra(extraKey, extraValue)
            is LongArray -> intent.putExtra(extraKey, extraValue)
            is CharArray -> intent.putExtra(extraKey, extraValue)
            is ShortArray -> intent.putExtra(extraKey, extraValue)
            is ByteArray -> intent.putExtra(extraKey, extraValue)
            is BooleanArray -> intent.putExtra(extraKey, extraValue)
            is Parcelable -> intent.putExtra(extraKey, extraValue)
            is Serializable -> intent.putExtra(extraKey, extraValue)
        }
    }
    context?.startActivity(intent)
}