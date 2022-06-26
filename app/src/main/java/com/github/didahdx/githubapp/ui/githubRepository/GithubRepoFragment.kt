package com.github.didahdx.githubapp.ui.githubRepository

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.githubapp.R
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.databinding.FragmentGithubRepoBinding
import com.github.didahdx.githubapp.ui.searchUsers.adapters.FooterLoadStateAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class GithubRepoFragment : Fragment(R.layout.fragment_github_repo) {

    private val viewModel: GithubRepoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGithubRepoBinding.bind(view)
        val repoAdapter = RepoAdapter(object : OnItemClickListener {
            override fun onClick(repositoryDto: RepositoryDto) {
                val url = repositoryDto.htmlUrl
                try {
                    val blogIntent = Intent(Intent.ACTION_VIEW)
                    blogIntent.data = Uri.parse(url)
                    startActivity(blogIntent)
                } catch (e: Exception) {
                    Timber.e(e)
                    val message =
                        getString(R.string.no_application_available, url)
                    val snackbar =
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
        })


        viewLifecycleOwner.lifecycleScope.launch {
            repoAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && repoAdapter.itemCount == 0
                // show empty list
                binding.emptyList.isVisible = isListEmpty

                if(loadState.refresh is LoadState.Loading){
                    binding.rvRepos.isVisible = false
                }else{
                    // Only show the list if refresh succeeds.
                    binding.rvRepos.isVisible = !isListEmpty
                }
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_message, it.error),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        val itemDecoration = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvRepos.apply {
            adapter = repoAdapter.withLoadStateHeaderAndFooter(
                header = FooterLoadStateAdapter { repoAdapter.retry() },
                footer = FooterLoadStateAdapter { repoAdapter.retry() }
            )
            layoutManager = manager
            addItemDecoration(itemDecoration)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.githubRepository.collectLatest(repoAdapter::submitData)
        }

    }

}