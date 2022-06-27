package com.github.didahdx.githubapp.data.local.enitities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    @PrimaryKey
    val repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?,
    val endpoint: String
)

