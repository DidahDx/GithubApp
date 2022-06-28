package com.github.didahdx.githubapp.data.repository

import com.github.didahdx.githubapp.data.local.GithubDatabase
import com.github.didahdx.githubapp.data.remote.ApiServiceAbstract
import com.github.didahdx.githubapp.data.remote.api.GitHubApiService
import org.junit.After
import org.junit.Before
import org.junit.Test

class GithubUsersRepositoryTest {
    lateinit var githubUsersRepository: GithubUsersRepository
    lateinit var githubDatabase: GithubDatabase
    lateinit var gitHubApiService: GitHubApiService

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun searchUser() {
    }

    @Test
    fun getUserDetails() {
    }

    @Test
    fun getFollowerList() {
    }

    @Test
    fun getFollowingList() {
    }

    @Test
    fun getUsersRepositoryList() {
    }
}