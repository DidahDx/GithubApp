package com.github.didahdx.githubapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.didahdx.githubapp.data.local.enitities.RepositoryEntity

@Dao
interface RepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepositoryEntity>)

    @Query("SELECT * FROM RepositoryEntity WHERE login LIKE :query ORDER by idLocal ASC")
    fun getRepositoryBy(query: String): PagingSource<Int, RepositoryEntity>

    @Query("DELETE FROM RepositoryEntity")
    suspend fun clearRepo()
}