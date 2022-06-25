package com.github.didahdx.githubapp.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Permissions(
    @Json(name = "admin")
    val admin: Boolean,
    @Json(name = "pull")
    val pull: Boolean,
    @Json(name = "push")
    val push: Boolean
)