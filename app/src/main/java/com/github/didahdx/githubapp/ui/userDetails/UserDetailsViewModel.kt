package com.github.didahdx.githubapp.ui.userDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.didahdx.githubapp.common.util.Resource
import com.github.didahdx.githubapp.data.local.enitities.UserDetailsEntity
import com.github.didahdx.githubapp.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val LOGIN = "login"
    }

    val userDetail = MutableLiveData<Resource<UserDetailsEntity>>()
    private val login = savedStateHandle.getLiveData(LOGIN, "")

    init {
        getUserDetails()
    }

    fun getUserDetails() {
        usersRepository.getUserDetails(login.value!!).onEach {
            userDetail.postValue(it)
        }.launchIn(viewModelScope)
    }
}