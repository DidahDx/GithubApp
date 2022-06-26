package com.github.didahdx.githubapp.data.repository

import androidx.paging.PagingData
import com.github.didahdx.githubapp.common.Constants
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.data.remote.dto.UserDetails
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun searchUser(
        user: String
    ): Flow<PagingData<User>>


     fun getUserDetails(
        userName: String
    ): Flow<UserDetails>


    fun getFollowerList(
        userName: String
    ): Flow<PagingData<User>>


    fun getFollowingList(
        userName: String
    ): Flow<PagingData<User>>


    fun getUsersRepositoryList(
        userName: String
    ): Flow<PagingData<RepositoryDto>>
}