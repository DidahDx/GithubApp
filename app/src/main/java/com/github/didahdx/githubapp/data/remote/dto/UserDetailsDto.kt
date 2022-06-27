package com.github.didahdx.githubapp.data.remote.dto


import com.github.didahdx.githubapp.data.local.enitities.UserDetailsEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDetailsDto(
    @Json(name = "avatar_url")
    val avatarUrl: String,
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "blog")
    val blog: String?,
    @Json(name = "collaborators")
    val collaborators: Int?,
    @Json(name = "company")
    val company: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "followers")
    val followers: Int,
    @Json(name = "following")
    val following: Int,
    @Json(name = "html_url")
    val htmlUrl: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "location")
    val location: String?,
    @Json(name = "login")
    val login: String,
    @Json(name = "name")
    val name: String?,
    @Json(name = "public_gists")
    val publicGists: Int?,
    @Json(name = "public_repos")
    val publicRepos: Int?,
    @Json(name = "twitter_username")
    val twitterUsername: String?,
) {

    fun mapToUserDetailsEntity(): UserDetailsEntity {
        return UserDetailsEntity(
            avatarUrl,
            bio,
            blog,
            collaborators,
            company,
            email,
            followers,
            following,
            htmlUrl,
            id,
            location,
            login,
            name,
            publicGists,
            publicRepos,
            twitterUsername
        )
    }
}