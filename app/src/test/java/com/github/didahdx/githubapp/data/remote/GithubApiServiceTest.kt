package com.github.didahdx.githubapp.data.remote

import com.github.didahdx.githubapp.ApiServiceAbstract
import com.github.didahdx.githubapp.data.remote.api.GitHubApiService
import com.github.didahdx.githubapp.data.remote.dto.UserDetailsDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GithubApiServiceTest : ApiServiceAbstract<GitHubApiService>() {

    lateinit var apiService: GitHubApiService

    @Before
    fun setUp() {
        apiService = createService(GitHubApiService::class.java)
    }


    @Test
    fun getUserDetails() = runBlocking {
        val userDetails = UserDetailsDto(
            avatarUrl = "https://avatars.githubusercontent.com/u/635?v=4",
            bio = "Co-founder & Developer at Overstellar",
            blog = "https://www.overstellar.se/",
            collaborators = null,
            company = "Overstellar",
            email = null,
            followers = 36,
            following = 1,
            htmlUrl = "https://github.com/daniel",
            id = 635,
            location = "Enköping",
            login = "daniel",
            name = "Daniel Eriksson",
            publicGists = 4,
            publicRepos = 41,
            twitterUsername = "danieleriksson"
        )
        val username = "daniel"
        enqueueResponse("userDetail.json")
        val userDetailDto = apiService.getUserDetails(username)

        assertEquals(userDetailDto, userDetails)
    }

    @Test
    fun getFollowers() = runBlocking {
        enqueueResponse("followers.json")
        val followers = apiService.getFollowerUsersList("didahdx",1,30)
        assertEquals(followers.size,30)
    }

    @Test
    fun getFollowing()= runBlocking {
        enqueueResponse("following.json")
        val following = apiService.getFollowingUsersList("didahdx",1,30)
        val user =following.find { it.login=="JakeWharton" }
        assertEquals(user?.login,"JakeWharton")
    }

    @Test
    fun getRepository()= runBlocking {
        enqueueResponse("repository.json")
        val repos = apiService.getUsersRepositoryList("didahdx",1,30)
        assertEquals(30,repos.size)
    }
}