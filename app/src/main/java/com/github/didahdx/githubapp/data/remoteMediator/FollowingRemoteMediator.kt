package com.github.didahdx.githubapp.data.remoteMediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.github.didahdx.githubapp.common.Constants
import com.github.didahdx.githubapp.data.local.GithubDatabase
import com.github.didahdx.githubapp.data.local.dao.FollowingDao
import com.github.didahdx.githubapp.data.local.dao.RemoteKeysDao
import com.github.didahdx.githubapp.data.local.enitities.FollowingEntity
import com.github.didahdx.githubapp.data.local.enitities.RemoteKeys
import com.github.didahdx.githubapp.data.remote.api.GitHubApiService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class FollowingRemoteMediator constructor(
    private val gitHubApiService: GitHubApiService,
    private val query: String,
    private val followingDao: FollowingDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val githubDatabase: GithubDatabase
) : RemoteMediator<Int, FollowingEntity>() {

    companion object {
        const val FOLLOWING = "FOLLOWING"
    }

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FollowingEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.FIRST_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val repos = gitHubApiService.getFollowingUsersList(query, page, state.config.pageSize)

            val endOfPaginationReached = repos.isEmpty()
            githubDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys(FOLLOWING)
                    followingDao.clearUsers()
                }
                val prevKey = if (page == Constants.FIRST_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = repos.map {
                    RemoteKeys(
                        repoId = it.id.toLong(),
                        prevKey = prevKey,
                        nextKey = nextKey,
                        FOLLOWING
                    )
                }
                remoteKeysDao.insertAll(keys)
                followingDao.insertAll(repos.map { it.mapToUserEntityForFollowings(query) })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, FollowingEntity>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                remoteKeysDao.remoteKeysRepoId(repoId.toLong(), FOLLOWING)
            }
        }
    }


    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, FollowingEntity>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                remoteKeysDao.remoteKeysRepoId(repo.id.toLong(), FOLLOWING)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, FollowingEntity>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                remoteKeysDao.remoteKeysRepoId(repo.id.toLong(), FOLLOWING)
            }
    }

}