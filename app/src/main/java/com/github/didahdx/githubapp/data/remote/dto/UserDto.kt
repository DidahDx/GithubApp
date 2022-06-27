package com.github.didahdx.githubapp.data.remote.dto


import com.github.didahdx.githubapp.data.local.enitities.FollowersEntity
import com.github.didahdx.githubapp.data.local.enitities.FollowingEntity
import com.github.didahdx.githubapp.data.local.enitities.SearchUserEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "html_url")
    val htmlUrl: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "login")
    val login: String
) {

    fun mapToSearchUserEntity(): SearchUserEntity {
        return SearchUserEntity(avatarUrl, htmlUrl, id, login)
    }

    fun mapToUserEntityForFollowers(username: String): FollowersEntity {
        return FollowersEntity(avatarUrl, htmlUrl, id, login, username)
    }

    fun mapToUserEntityForFollowings(username: String): FollowingEntity {
        return FollowingEntity(avatarUrl, htmlUrl, id, login, username)
    }
}