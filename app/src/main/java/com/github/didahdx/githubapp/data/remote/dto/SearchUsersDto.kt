package com.github.didahdx.githubapp.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchUsersDto(
    @Json(name = "incomplete_results")
    val incompleteResults: Boolean,
    @Json(name = "items")
    val items: List<UserDto>,
    @Json(name = "total_count")
    val totalCount: Int
)