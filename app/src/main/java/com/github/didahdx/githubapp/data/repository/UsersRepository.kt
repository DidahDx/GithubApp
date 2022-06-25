package com.github.didahdx.githubapp.data.repository

import com.github.didahdx.githubapp.common.Constants
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.data.remote.dto.UserDetails
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun searchUser(
        user: String,
        page: Int = 1,
        pageSize: Int = Constants.PAGE_SIZE
    ): Flow<List<User>>


    suspend fun getUserDetails(
        userName: String,
        page: Int = 1,
        pageSize: Int = Constants.PAGE_SIZE
    ): UserDetails


    suspend fun getFollowerList(
        userName: String,
        page: Int = 1,
        pageSize: Int = Constants.PAGE_SIZE
    ): List<User>


    suspend fun getFollowingList(
        userName: String,
        page: Int = 1,
        pageSize: Int = Constants.PAGE_SIZE
    ): List<User>


    suspend fun getUsersRepositoryList(
        userName: String,
        page: Int = 1,
        pageSize: Int = Constants.PAGE_SIZE
    ): List<RepositoryDto>
}