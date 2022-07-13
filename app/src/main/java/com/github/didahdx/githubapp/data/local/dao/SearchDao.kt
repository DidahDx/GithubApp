package com.github.didahdx.githubapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.didahdx.githubapp.data.local.enitities.SearchUserEntity

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<SearchUserEntity>)

    @Query("SELECT * FROM SearchUserEntity  WHERE login LIKE  '%' || :user || '%' ORDER by idLocal ASC")
    fun getUsersByQuery(user: String): PagingSource<Int, SearchUserEntity>

    @Query("DELETE  FROM SearchUserEntity")
    suspend fun clearUsers()
}