package com.github.didahdx.githubapp.ui.searchUsers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.didahdx.githubapp.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val users = searchUserQuery.asFlow()
        .flatMapLatest { query ->
            usersRepository.searchUser(query)
        }.cachedIn(viewModelScope)

    fun searchUser(query: String) {
        searchUserQuery.value = query
    }
}