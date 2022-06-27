package com.github.didahdx.githubapp.data.model

data class User(
    val avatarUrl: String,
    val htmlUrl: String,
    val id: Int,
    val login: String,
    var username: String?
)
