package com.github.didahdx.githubapp.ui.follow

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.data.repository.UsersRepository
import com.github.didahdx.githubapp.ui.follow.FollowFragment.Companion.IS_FOLLOWING
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class FollowViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private var login = savedStateHandle.getLiveData(UserDetailsViewModel.LOGIN, "")
    var isFollowing = savedStateHandle.getLiveData(IS_FOLLOWING, true)
    val user = MutableLiveData<List<User>>()

    val followerList = login.asFlow().flatMapLatest { userId ->
        usersRepository.getFollowerList(userId)
    }.cachedIn(viewModelScope)

    val followingList = login.asFlow().flatMapLatest { userId ->
        usersRepository.getFollowingList(userId)
    }.cachedIn(viewModelScope)


}