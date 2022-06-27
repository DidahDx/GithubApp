package com.github.didahdx.githubapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.didahdx.githubapp.data.local.enitities.UserDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userDetails: UserDetailsEntity)

    @Query("SELECT * FROM UserDetailsEntity WHERE login=:login")
    fun getUserDetailByLogin(login: String): Flow<UserDetailsEntity>

    @Query("DELETE FROM UserDetailsEntity")
    suspend fun clearUserDetails()
}