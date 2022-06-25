package com.github.didahdx.githubapp.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.didahdx.githubapp.common.Constants
import com.github.didahdx.githubapp.data.remote.api.GitHubApiService
import com.github.didahdx.githubapp.data.remote.dto.User
import retrofit2.HttpException
import java.io.IOException

class FollowingPagingSource constructor(
    private val gitHubApiService: GitHubApiService,
    private val query: String
) : PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: Constants.FIRST_PAGE
        return try {
            val repos = gitHubApiService.getFollowingUsersList(query, position, params.loadSize)
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / Constants.PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == Constants.FIRST_PAGE) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }

    }
}