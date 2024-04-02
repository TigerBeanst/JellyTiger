package fail.tiger.jellytiger

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.tencent.mmkv.MMKV
import fail.tiger.jellytiger.data.AppDatabase
import fail.tiger.jellytiger.data.JellyTiger
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo

class JellyTigerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        val jtDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "jellytiger-db"
        ).allowMainThreadQueries().build()
        jellyTiger = JellyTiger(
            jellyfin = createJellyfin {
                context = appContext
                clientInfo = ClientInfo(name = "JellyTiger", version = BuildConfig.VERSION_NAME)
            },
            dbServer = jtDatabase.serverDao(),
            dbUser = jtDatabase.userDao()
        )
        MMKV.initialize(applicationContext)
        mmkv = MMKV.defaultMMKV()
    }

    companion object {
        lateinit var appContext: Context
        lateinit var jellyTiger: JellyTiger
        lateinit var mmkv: MMKV
    }
}