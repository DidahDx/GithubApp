package com.github.didahdx.githubapp.ui.githubRepository

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.githubapp.R
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.databinding.FragmentGithubRepoBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class GithubRepoFragment : Fragment(R.layout.fragment_github_repo) {

    private val viewModel: GithubRepoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentGithubRepoBinding.bind(view)
        val repoAdapter = RepoAdapter(object :OnItemClickListener{
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
        val itemDecoration = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvRepos.apply {
            adapter = repoAdapter
            layoutManager = manager
            addItemDecoration(itemDecoration)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.githubRepository.collectLatest(repoAdapter::submitData)
        }

    }

}