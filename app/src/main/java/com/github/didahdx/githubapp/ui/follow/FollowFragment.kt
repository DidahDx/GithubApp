package com.github.didahdx.githubapp.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.didahdx.githubapp.R
import com.github.didahdx.githubapp.common.extension.navigateSafe
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.databinding.FragmentFollowBinding
import com.github.didahdx.githubapp.ui.searchUsers.OnItemClickListener
import com.github.didahdx.githubapp.ui.searchUsers.UsersAdapter
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowFragment : Fragment() {
    companion object {
        const val IS_FOLLOWING = "is_following"
    }

    private val viewModel: FollowViewModel by viewModels()

    private var _binding : FragmentFollowBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =FragmentFollowBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userAdapter = UsersAdapter(object : OnItemClickListener{
            override fun userClicked(user: User) {
                val bundle = bundleOf(UserDetailsViewModel.LOGIN to user.login)
                findNavController().navigateSafe(
                    R.id.action_followFragment_to_userDetailsFragment,
                    bundle
                )
            }

        })
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvFollows.apply {
            adapter = userAdapter
            layoutManager = manager
        }

        viewModel.isFollowing.observe(viewLifecycleOwner){
            if(it){

            }else{

            }
        }
        viewModel.user.observe(viewLifecycleOwner) {
            userAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}