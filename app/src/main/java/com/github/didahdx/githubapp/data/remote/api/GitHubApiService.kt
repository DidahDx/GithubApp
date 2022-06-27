package com.github.didahdx.githubapp.data.remote.api

import com.github.didahdx.githubapp.BuildConfig.GITHUB_TOKEN
import com.github.didahdx.githubapp.common.Constants
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.remote.dto.SearchUsersDto
import com.github.didahdx.githubapp.data.remote.dto.UserDetailsDto
import com.github.didahdx.githubapp.data.remote.dto.UserDto
import retrofit2.http.*

interface GitHubApiService {


    @Headers("Accept: application/vnd.github.v3+json")
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") user: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
        @HeaderMap headers: Map<String, String> = mapOf("Authorization" to " token $GITHUB_TOKEN")
    ): SearchUsersDto

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("users/{username}")
    suspend fun getUserDetails(
        @Path("username") userName: String,
        @HeaderMap headers: Map<String, String> = mapOf("Authorization" to " token $GITHUB_TOKEN")
    ): UserDetailsDto

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("users/{username}/followers")
    suspend fun getFollowerUsersList(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
        @HeaderMap headers: Map<String, String> = mapOf("Authorization" to " token $GITHUB_TOKEN")
    ): List<UserDto>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("users/{username}/following")
    suspend fun getFollowingUsersList(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
        @HeaderMap headers: Map<String, String> = mapOf("Authorization" to " token $GITHUB_TOKEN")
    ): List<UserDto>

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("users/{username}/repos")
    suspend fun getUsersRepositoryList(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
        @Query("sort") sortBy: String = Constants.DEFAULT_SORT_GITHUB,
        @Query("direction") orderBy: String = Constants.DEFAULT_ORDER_BY,
        @HeaderMap headers: Map<String, String> = mapOf("Authorization" to " token $GITHUB_TOKEN")
    ): List<RepositoryDto>
}