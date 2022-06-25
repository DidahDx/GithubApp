package com.github.didahdx.githubapp.ui.searchUsers

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.githubapp.R
import com.github.didahdx.githubapp.common.extension.navigateSafe
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.databinding.FragmentSearchBinding
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchBinding.bind(view)
        val searchAdapter = UsersAdapter(object : OnItemClickListener {
            override fun userClicked(user: User) {
                val bundle = bundleOf(UserDetailsViewModel.LOGIN to user.login)
                findNavController().navigateSafe(
                    R.id.action_searchFragment_to_userDetailsFragment,
                    bundle
                )
                Timber.e(user.toString())
            }
        })

        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvUsers.apply {
            adapter = searchAdapter
            layoutManager = manager
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.users.collectLatest(searchAdapter::submitData)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && query.isNotEmpty()) {
                    viewModel.searchUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

}