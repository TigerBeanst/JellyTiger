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
data class Server(
    @PrimaryKey @ColumnInfo(name = "server_id") val serverId: String,
    @ColumnInfo(name = "server_name") val serverName: String?,
    @ColumnInfo(name = "server_address") val serverAddress: String?,
    @ColumnInfo(name = "server_local_address") val serverLocalUrl: String?,
    @ColumnInfo(name = "server_local_ssid") val serverLocalSsid: String?,
    @ColumnInfo(name = "server_jf_version") val serverJfVersion: String?
)

@Dao
interface ServerDao {
    @Query("SELECT * FROM server")
    fun getAll(): List<Server>

    @Query("SELECT * FROM server WHERE server_id = :serverId")
    fun getByServerId(serverId: String): List<Server>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(server: Server)

    @Delete
    fun delete(server: Server)
}