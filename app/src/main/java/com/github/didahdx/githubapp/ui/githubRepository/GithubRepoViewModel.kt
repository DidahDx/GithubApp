package com.github.didahdx.githubapp.ui.githubRepository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.repository.UsersRepository
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubRepoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val login = savedStateHandle.getLiveData(UserDetailsViewModel.LOGIN, "")

    val githubRepository = MutableLiveData<List<RepositoryDto>>()

    init {
        getRepositories()
    }

    fun getRepositories() {
        viewModelScope.launch {
            val repos = usersRepository.getUsersRepositoryList(login.value!!)
            githubRepository.postValue(repos)
        }
    }
}