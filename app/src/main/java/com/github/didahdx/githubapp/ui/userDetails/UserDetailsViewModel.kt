package com.github.didahdx.githubapp.ui.userDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.didahdx.githubapp.data.remote.dto.UserDetails
import com.github.didahdx.githubapp.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            val detail = usersRepository.getUserDetails(login.value!!)
            userDetail.postValue(detail)
        }
    }
}