package com.github.didahdx.githubapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.didahdx.githubapp.data.local.enitities.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM RemoteKeys WHERE (repoId =:repoId and endpoint =:endPoint)")
    suspend fun remoteKeysRepoId(repoId: Long, endPoint: String): RemoteKeys?

    @Query("DELETE FROM RemoteKeys WHERE endpoint =:endPoint")
    suspend fun clearRemoteKeys(endPoint: String)
}
