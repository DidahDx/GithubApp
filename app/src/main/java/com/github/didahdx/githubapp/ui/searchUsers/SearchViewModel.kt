package com.github.didahdx.githubapp.ui.searchUsers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.github.didahdx.githubapp.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private val searchUserQuery = savedStateHandle.getLiveData(SEARCH_QUERY, "")


    val users = searchUserQuery.switchMap { query ->
        usersRepository.searchUser(query).asLiveData()
    }

    fun searchUser(query: String) {
        searchUserQuery.value = query
    }
}