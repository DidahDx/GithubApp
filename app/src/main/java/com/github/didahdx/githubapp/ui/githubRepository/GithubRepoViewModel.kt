package com.github.didahdx.githubapp.ui.githubRepository

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.repository.UsersRepository
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubRepoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val login = savedStateHandle.getLiveData(UserDetailsViewModel.LOGIN, "")

    @OptIn(ExperimentalCoroutinesApi::class)
    val githubRepository = login.asFlow().flatMapLatest { userId ->
        usersRepository.getUsersRepositoryList(userId)
    }.cachedIn(viewModelScope)

}