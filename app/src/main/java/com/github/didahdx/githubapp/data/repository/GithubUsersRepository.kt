package com.github.didahdx.githubapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.github.didahdx.githubapp.common.Constants
import com.github.didahdx.githubapp.common.util.Network
import com.github.didahdx.githubapp.common.util.networkBoundResource
import com.github.didahdx.githubapp.data.local.GithubDatabase
import com.github.didahdx.githubapp.data.local.dao.*
import com.github.didahdx.githubapp.data.local.enitities.FollowersEntity
import com.github.didahdx.githubapp.data.local.enitities.FollowingEntity
import com.github.didahdx.githubapp.data.local.enitities.RepositoryEntity
import com.github.didahdx.githubapp.data.local.enitities.SearchUserEntity
import com.github.didahdx.githubapp.data.remote.api.GitHubApiService
import com.github.didahdx.githubapp.data.remote.dto.UserDetailsDto
import com.github.didahdx.githubapp.data.remoteMediator.FollowersRemoteMediator
import com.github.didahdx.githubapp.data.remoteMediator.FollowingRemoteMediator
import com.github.didahdx.githubapp.data.remoteMediator.RepositoryRemoteMediator
import com.github.didahdx.githubapp.data.remoteMediator.SearchRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubUsersRepository @Inject constructor(
    private val gitHubApiService: GitHubApiService,
    private val remoteKeysDao: RemoteKeysDao,
    private val githubDatabase: GithubDatabase,
    private val followerDao: FollowerDao,
    private val followingDao: FollowingDao,
    private val searchDao: SearchDao,
    private val userDetailsDao: UserDetailsDao,
    private val repositoryDao: RepositoryDao,
    private val network: Network
) : UsersRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun searchUser(user: String): Flow<PagingData<SearchUserEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = Constants.PAGE_MAX_SIZE
            ),
            remoteMediator = SearchRemoteMediator(
                gitHubApiService, user, searchDao, remoteKeysDao, githubDatabase
            ),
            pagingSourceFactory = { searchDao.getUsersByQuery() }
        ).flow
    }

    override fun getUserDetails(userName: String) = networkBoundResource(
        query = {
            userDetailsDao.getUserDetailByLogin(userName)
        },
        fetch = {
            gitHubApiService.getUserDetails(userName)
        },
        saveFetchResult = { userDetailsDto ->
            githubDatabase.withTransaction {
                userDetailsDao.clearUserDetails()
                userDetailsDao.insert(userDetailsDto.mapToUserDetailsEntity())
            }
        },
        shouldFetch = { _ ->
            network.isNetworkAvailable()
        }

    )

    @OptIn(ExperimentalPagingApi::class)
    override fun getFollowerList(userName: String): Flow<PagingData<FollowersEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = Constants.PAGE_MAX_SIZE
            ),
            remoteMediator = FollowersRemoteMediator(
                gitHubApiService,
                userName,
                followerDao,
                remoteKeysDao,
                githubDatabase
            ),
            pagingSourceFactory = { followerDao.getUsersByQuery(userName) }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getFollowingList(userName: String): Flow<PagingData<FollowingEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = Constants.PAGE_MAX_SIZE
            ),
            remoteMediator = FollowingRemoteMediator(
                gitHubApiService, userName, followingDao, remoteKeysDao, githubDatabase
            ),
            pagingSourceFactory = { followingDao.getUsersByQuery(userName) }
        ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getUsersRepositoryList(
        userName: String
    ): Flow<PagingData<RepositoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = Constants.PAGE_MAX_SIZE
            ),
            remoteMediator = RepositoryRemoteMediator(
                gitHubApiService,
                userName,
                repositoryDao,
                remoteKeysDao,
                githubDatabase
            ),
            pagingSourceFactory = { repositoryDao.getRepositoryBy(userName) }
        ).flow
    }
}