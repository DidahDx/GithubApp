package com.github.didahdx.githubapp.data.local.enitities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RepositoryEntity(
    val archived: Boolean?,
    val defaultBranch: String?,
    val description: String?,
    val fork: Boolean?,
    val forksCount: Int?,
    val fullName: String?,
    val homepage: String?,
    val htmlUrl: String?,
    val id: Int,
    val language: String?,
    val name: String?,
    val openIssuesCount: Int?,
    val stargazersCount: Int?,
    val topics: List<String?>?,
    val license: String?,
    val login: String
) {
    @PrimaryKey(autoGenerate = true)
    var idLocal: Long = 0
}
