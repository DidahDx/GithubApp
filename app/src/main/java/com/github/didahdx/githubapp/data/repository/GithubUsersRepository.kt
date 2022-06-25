package com.github.didahdx.githubapp.data.repository

import com.github.didahdx.githubapp.data.remote.api.GitHubApiService
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.data.remote.dto.UserDetails
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class GithubUsersRepository @Inject constructor(
    private val gitHubApiService: GitHubApiService
) : UsersRepository {
    override fun searchUser(user: String, page: Int, pageSize: Int) = flow {
        try {
            emit(gitHubApiService.searchUsers(user, page, pageSize).items)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun getUserDetails(userName: String, page: Int, pageSize: Int): UserDetails {
        return gitHubApiService.getUserDetails(userName, page, pageSize)
    }

    override suspend fun getFollowerList(userName: String, page: Int, pageSize: Int): List<User> {
        return gitHubApiService.getFollowerList(userName, page, pageSize)
    }

    override suspend fun getFollowingList(userName: String, page: Int, pageSize: Int): List<User> {
        return gitHubApiService.getFollowingList(userName, page, pageSize)
    }

    override suspend fun getUsersRepositoryList(
        userName: String,
        page: Int,
        pageSize: Int
    ): List<RepositoryDto> {
        return gitHubApiService.getUsersRepositoryList(userName, page, pageSize)
    }
}