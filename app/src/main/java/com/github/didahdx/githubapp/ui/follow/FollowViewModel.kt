package com.github.didahdx.githubapp.ui.follow

import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.map
import com.github.didahdx.githubapp.data.remote.dto.UserDto
import com.github.didahdx.githubapp.data.repository.UsersRepository
import com.github.didahdx.githubapp.ui.follow.FollowFragment.Companion.IS_FOLLOWING
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FollowViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private var login: MutableLiveData<String> =
        savedStateHandle.getLiveData(UserDetailsViewModel.LOGIN)
    var isFollowing = savedStateHandle.getLiveData(IS_FOLLOWING, true)
    val user = MutableLiveData<List<UserDto>>()

    val followerList = login.asFlow().flatMapLatest { userId ->
        usersRepository.getFollowerList(userId)
            .map { pagingData -> pagingData.map { it.mapToUser() } }
    }.cachedIn(viewModelScope)

    val followingList = login.asFlow().flatMapLatest { userId ->
        usersRepository.getFollowingList(userId)
            .map { pagingData -> pagingData.map { it.mapToUser() } }
    }.cachedIn(viewModelScope)


}