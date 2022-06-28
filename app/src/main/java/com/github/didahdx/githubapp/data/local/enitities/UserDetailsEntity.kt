package com.github.didahdx.githubapp.data.local.enitities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDetailsEntity(
    val avatarUrl: String,
    val bio: String?,
    val blog: String?,
    val collaborators: Int?,
    val company: String?,
    val email: String?,
    val followers: Int,
    val following: Int,
    val htmlUrl: String,
    @PrimaryKey val id: Int,
    val location: String?,
    val login: String,
    val name: String?,
    val publicGists: Int?,
    val publicRepos: Int?,
    val twitterUsername: String?,
)