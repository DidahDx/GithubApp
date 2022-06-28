package com.github.didahdx.githubapp.ui.searchUsers

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.githubapp.R
import com.github.didahdx.githubapp.common.extension.navigateSafe
import com.github.didahdx.githubapp.common.extension.onQueryTextSubmit
import com.github.didahdx.githubapp.data.local.enitities.SearchUserEntity
import com.github.didahdx.githubapp.databinding.FragmentSearchBinding
import com.github.didahdx.githubapp.ui.searchUsers.adapters.FooterLoadStateAdapter
import com.github.didahdx.githubapp.ui.searchUsers.adapters.OnItemClick
import com.github.didahdx.githubapp.ui.searchUsers.adapters.SearchUsersAdapter
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @OptIn(InternalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        val searchUsersAdapter = SearchUsersAdapter(object : OnItemClick {
            override fun userClicked(user: SearchUserEntity) {
                val bundle = bundleOf(UserDetailsViewModel.LOGIN to user.login)
                findNavController().navigateSafe(
                    R.id.action_searchFragment_to_userDetailsFragment,
                    bundle
                )
            }
        })

        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val header = FooterLoadStateAdapter { searchUsersAdapter.retry() }
        binding.rvUsers.apply {
            adapter = searchUsersAdapter.withLoadStateHeaderAndFooter(
                header = header,
                footer = FooterLoadStateAdapter { searchUsersAdapter.retry() }
            )
            layoutManager = manager
        }

        viewLifecycleOwner.lifecycleScope.launch {
            searchUsersAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && searchUsersAdapter.itemCount == 0
                // show empty list
                binding.emptyList.isVisible = isListEmpty

                // Only show the list if refresh succeeds.
                binding.rvUsers.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading

                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                binding.retryButton.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && searchUsersAdapter.itemCount == 0


                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && searchUsersAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_message, it.error.localizedMessage?: R.string.something_went_wrong),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }



        binding.retryButton.setOnClickListener {
            searchUsersAdapter.retry()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.users.collectLatest(searchUsersAdapter::submitData)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchUserQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }
        searchView.onQueryTextSubmit { query ->
            viewModel.searchUser(query)
            searchView.clearFocus()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvUsers.adapter = null
        _binding = null
    }

}