package com.github.didahdx.githubapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.didahdx.githubapp.data.local.enitities.FollowersEntity

@Dao
interface FollowerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<FollowersEntity>)

    @Query("SELECT * FROM FollowersEntity WHERE username LIKE :username ORDER by idLocal ASC")
    fun getUsersByQuery(username: String): PagingSource<Int, FollowersEntity>

    @Query("DELETE  FROM FollowersEntity")
    suspend fun clearUsers()
}