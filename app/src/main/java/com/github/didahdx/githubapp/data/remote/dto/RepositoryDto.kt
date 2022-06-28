package com.github.didahdx.githubapp.data.remote.dto


import com.github.didahdx.githubapp.data.local.enitities.RepositoryEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepositoryDto(
    @Json(name = "archived")
    val archived: Boolean?,
    @Json(name = "default_branch")
    val defaultBranch: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "fork")
    val fork: Boolean?,
    @Json(name = "forks_count")
    val forksCount: Int?,
    @Json(name = "full_name")
    val fullName: String?,
    @Json(name = "homepage")
    val homepage: String?,
    @Json(name = "html_url")
    val htmlUrl: String?,
    @Json(name = "id")
    val id: Int,
    @Json(name = "owner")
    val owner: OwnerDto,
    @Json(name = "language")
    val language: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "open_issues_count")
    val openIssuesCount: Int?,
    @Json(name = "stargazers_count")
    val stargazersCount: Int?,
    @Json(name = "topics")
    val topics: List<String>?,
    @Json(name = "license")
    val license: LicenseDto?
) {

    fun mapToRepositoryEntity(): RepositoryEntity {
        return RepositoryEntity(
            archived,
            defaultBranch,
            description,
            fork,
            forksCount,
            fullName,
            homepage,
            htmlUrl,
            id,
            language,
            name,
            openIssuesCount,
            stargazersCount,
            topics,
            license?.name,
            owner.login
        )
    }
}