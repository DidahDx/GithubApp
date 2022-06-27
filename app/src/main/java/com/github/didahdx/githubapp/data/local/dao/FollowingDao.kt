package com.github.didahdx.githubapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.didahdx.githubapp.data.local.enitities.FollowingEntity

@Dao
interface FollowingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(following: List<FollowingEntity>)

    @Query("SELECT * FROM FollowingEntity WHERE username LIKE :user ORDER by idLocal ASC")
    fun getUsersByQuery(user: String): PagingSource<Int, FollowingEntity>


    @Query("DELETE  FROM FollowingEntity")
    suspend fun clearUsers()
}