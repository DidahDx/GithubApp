package com.github.didahdx.githubapp.ui.follow

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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
import com.github.didahdx.githubapp.data.model.User
import com.github.didahdx.githubapp.databinding.FragmentFollowBinding
import com.github.didahdx.githubapp.ui.searchUsers.adapters.FooterLoadStateAdapter
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FollowFragment : Fragment(R.layout.fragment_follow) {
    companion object {
        const val IS_FOLLOWING = "is_following"
        const val TITLE="title"
    }

    private val viewModel: FollowViewModel by viewModels()
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowBinding.bind(view)
        val followsAdapter = UserAdapter(object : OnItemClick {
            override fun userClicked(user: User) {
              val url = user.htmlUrl
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


        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        val header = FooterLoadStateAdapter { followsAdapter.retry() }
        binding.rvFollows.apply {
            adapter = followsAdapter.withLoadStateHeaderAndFooter(
                header = header,
                footer = FooterLoadStateAdapter { followsAdapter.retry() }
            )

            layoutManager = manager
        }

        viewLifecycleOwner.lifecycleScope.launch {
            followsAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && followsAdapter.itemCount == 0
                // show empty list
                binding.emptyList.isVisible = isListEmpty

                binding.rvFollows.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading

                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                binding.retryButton.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && followsAdapter.itemCount == 0

                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && followsAdapter.itemCount > 0 }
                    ?: loadState.prepend

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_message, it.error.localizedMessage ?: getString(R.string.something_went_wrong)),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }


        binding.retryButton.setOnClickListener {
            followsAdapter.retry()
        }

        viewModel.isFollowing.observe(viewLifecycleOwner) {
            if (it) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.followingList.collectLatest(followsAdapter::submitData)
                }
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.followerList.collectLatest(followsAdapter::submitData)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvFollows.adapter = null
        _binding = null
    }

}