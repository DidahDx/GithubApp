package com.github.didahdx.githubapp.data.repository

import androidx.paging.PagingData
import com.github.didahdx.githubapp.common.util.Resource
import com.github.didahdx.githubapp.data.local.enitities.*
import com.github.didahdx.githubapp.data.remote.dto.UserDetailsDto
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun searchUser(
        user: String
    ): Flow<PagingData<SearchUserEntity>>


    fun getUserDetails(
        userName: String
    ): Flow<Resource<UserDetailsEntity>>


    fun getFollowerList(
        userName: String
    ): Flow<PagingData<FollowersEntity>>


    fun getFollowingList(
        userName: String
    ): Flow<PagingData<FollowingEntity>>


    fun getUsersRepositoryList(
        userName: String
    ): Flow<PagingData<RepositoryEntity>>
}