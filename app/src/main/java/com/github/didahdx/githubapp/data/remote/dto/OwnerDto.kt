package com.github.didahdx.githubapp.data.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OwnerDto(
    @Json(name = "login")
    val login: String
)