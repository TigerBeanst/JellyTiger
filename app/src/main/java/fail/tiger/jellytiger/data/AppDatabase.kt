package fail.tiger.jellytiger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import fail.tiger.jellytiger.data.db.Server
import fail.tiger.jellytiger.data.db.ServerDao
import fail.tiger.jellytiger.data.db.User
import fail.tiger.jellytiger.data.db.UserDao

@Database(entities = [Server::class, User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
    abstract fun userDao(): UserDao
}