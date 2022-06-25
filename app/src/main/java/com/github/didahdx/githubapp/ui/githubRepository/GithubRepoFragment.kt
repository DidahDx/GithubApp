package com.github.didahdx.githubapp.ui.githubRepository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.githubapp.databinding.FragmentGithubRepoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GithubRepoFragment : Fragment() {

    private val viewModel: GithubRepoViewModel by viewModels()
    private var _binding: FragmentGithubRepoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGithubRepoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val repoAdapter = RepoAdapter()
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
        viewModel.githubRepository.observe(viewLifecycleOwner) {
            repoAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}