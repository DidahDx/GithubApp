package com.github.didahdx.githubapp.data.remote.api

import com.github.didahdx.githubapp.common.Constants
import com.github.didahdx.githubapp.common.Constants.ACCEPT_HEADER
import com.github.didahdx.githubapp.common.Constants.GITHUB_TOKEN
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.remote.dto.SearchUsers
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.data.remote.dto.UserDetails
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApiService {


    @Headers("Accept: $ACCEPT_HEADER", "Authorization: token $GITHUB_TOKEN")
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") user: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): SearchUsers

    @Headers("Accept: $ACCEPT_HEADER", "Authorization: token $GITHUB_TOKEN")
    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") userName: String
    ): UserDetails

    @Headers("Accept: $ACCEPT_HEADER", "Authorization: token $GITHUB_TOKEN")
    @GET("users/{username}/followers")
    suspend fun getFollowerUsersList(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<User>

    @Headers("Accept: $ACCEPT_HEADER", "Authorization: token $GITHUB_TOKEN")
    @GET("users/{username}/following")
    suspend fun getFollowingUsersList(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<User>

    @Headers("Accept: $ACCEPT_HEADER", "Authorization: token $GITHUB_TOKEN")
    @GET("users/{username}/repos")
    suspend fun getUsersRepositoryList(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
        @Query("sort") sortBy: String = Constants.DEFAULT_SORT_GITHUB,
        @Query("direction") orderBy: String = Constants.DEFAULT_ORDER_BY
    ): List<RepositoryDto>
}