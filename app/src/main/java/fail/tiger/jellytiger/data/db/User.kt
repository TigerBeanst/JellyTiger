package fail.tiger.jellytiger.data.db

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "server_id") val serverId: String,
    @ColumnInfo(name = "access_token") val accessToken: String?,
    @ColumnInfo(name = "user_name") val userName: String?,
    @ColumnInfo(name = "primary_image_tag") val primaryImageTag: String?,
    @ColumnInfo(name = "is_administrator") val isAdministrator: Boolean?,
    @ColumnInfo(name = "is_disable") val isDisable: Boolean?
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE user_id = :userId")
    fun getByUserId(userId: String): List<User>

    @Query("SELECT * FROM user WHERE server_id = :serverId")
    fun getByServerId(serverId: String): List<User>

    @Query("SELECT * FROM user WHERE access_token = :accessToken")
    fun getByAccessToken(accessToken: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}
