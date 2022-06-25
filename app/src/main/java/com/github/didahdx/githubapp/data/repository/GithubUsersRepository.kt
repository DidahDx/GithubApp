package com.github.didahdx.githubapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.didahdx.githubapp.common.Constants
import com.github.didahdx.githubapp.data.pagingSource.FollowersPagingSource
import com.github.didahdx.githubapp.data.pagingSource.FollowingPagingSource
import com.github.didahdx.githubapp.data.pagingSource.RepositoryPagingSource
import com.github.didahdx.githubapp.data.pagingSource.SearchPagingSource
import com.github.didahdx.githubapp.data.remote.api.GitHubApiService
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.data.remote.dto.UserDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubUsersRepository @Inject constructor(
    private val gitHubApiService: GitHubApiService
) : UsersRepository {

    override fun searchUser(user: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchPagingSource(gitHubApiService, user) }
        ).flow
    }

    override suspend fun getUserDetails(userName: String): UserDetails {
        return gitHubApiService.getUserDetails(userName)
    }

    override fun getFollowerList(userName: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FollowersPagingSource(gitHubApiService, userName) }
        ).flow
    }

    override fun getFollowingList(userName: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { FollowingPagingSource(gitHubApiService, userName) }
        ).flow
    }

    override fun getUsersRepositoryList(
        userName: String
    ): Flow<PagingData<RepositoryDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RepositoryPagingSource(gitHubApiService, userName) }
        ).flow
    }
}