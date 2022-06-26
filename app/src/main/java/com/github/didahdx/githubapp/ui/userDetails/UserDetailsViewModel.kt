package com.github.didahdx.githubapp.ui.userDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.didahdx.githubapp.data.remote.dto.UserDetails
import com.github.didahdx.githubapp.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val LOGIN = "login"
    }

    val userDetail = MutableLiveData<UserDetails>()
    private val login = savedStateHandle.getLiveData(LOGIN, "")

    init {
        getUserDetails()
    }

    fun getUserDetails() {
        try {
            usersRepository.getUserDetails(login.value!!).onEach {
                userDetail.postValue(it)
            }.launchIn(viewModelScope)
        } catch (e: Exception) {
            Timber.e(e)
        }

    }
}