package ru.practicum.android.diploma.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.practicum.android.diploma.data.db.entity.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("Select * From remote_key Where vacancy_id = :id")
    suspend fun getRemoteKeyByVacancyID(id: Int): RemoteKeys?

    @Query("Delete From remote_key")
    suspend fun clearRemoteKeys()

//    @Query("Select created_at From remote_key Order By created_at DESC LIMIT 1")
//    suspend fun getCreationTime(): Long?
}
