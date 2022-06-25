package com.github.didahdx.githubapp.ui.follow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.data.repository.UsersRepository
import com.github.didahdx.githubapp.ui.follow.FollowFragment.Companion.IS_FOLLOWING
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository
): ViewModel() {

    private var login = savedStateHandle.getLiveData(UserDetailsViewModel.LOGIN, "")
     var isFollowing = savedStateHandle.getLiveData(IS_FOLLOWING,true)
    val user = MutableLiveData<List<User>>()

    init {
        getFollows()
    }

    fun getFollows(){
        if(isFollowing.value!!){
            getFollowing()
        }else{
            getFollowers()
        }
    }

    private fun getFollowers(){
        viewModelScope.launch {
            val followers = usersRepository.getFollowerList(login.value!!)
            user.postValue(followers)
        }
    }

    private fun getFollowing(){
        viewModelScope.launch{
            val following = usersRepository.getFollowingList(login.value!!)
            user.postValue(following)
        }
    }
}