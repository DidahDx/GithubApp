package com.github.didahdx.githubapp.ui.userDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.didahdx.githubapp.R
import com.github.didahdx.githubapp.common.extension.displayDataIfNotNull
import com.github.didahdx.githubapp.common.extension.hide
import com.github.didahdx.githubapp.common.extension.navigateSafe
import com.github.didahdx.githubapp.common.extension.show
import com.github.didahdx.githubapp.common.util.Resource
import com.github.didahdx.githubapp.data.local.enitities.UserDetailsEntity
import com.github.didahdx.githubapp.databinding.FragmentUserDetailsBinding
import com.github.didahdx.githubapp.ui.follow.FollowFragment.Companion.IS_FOLLOWING
import com.github.didahdx.githubapp.ui.follow.FollowFragment.Companion.TITLE
import com.github.didahdx.githubapp.ui.userDetails.UserDetailsViewModel.Companion.LOGIN
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {

    private val viewModel: UserDetailsViewModel by viewModels()

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserDetailsBinding.bind(view)
        viewModel.userDetail.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    binding.groupViews.hide()
                    binding.retryButton.show()
                    binding.progressBar.hide()
                    binding.emptyList.hide()
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_message, resource.error?.localizedMessage?: R.string.something_went_wrong),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                is Resource.Loading -> {
                    binding.groupViews.hide()
                    binding.retryButton.hide()
                    binding.progressBar.show()
                    binding.emptyList.hide()
                }
                is Resource.Success -> {
//                    binding.groupViews.show()
                    binding.retryButton.hide()
                    binding.progressBar.hide()
                    binding.emptyList.hide()
                    setUpData(resource.data)
                }
            }


        }

        binding.retryButton.setOnClickListener {
            viewModel.getUserDetails()
        }

    }


    private fun setUpData(userDetail: UserDetailsEntity?) {
        if (userDetail != null) {
            binding.groupViews.show()

            Glide.with(requireContext())
                .load(userDetail.avatarUrl)
                .centerCrop()
                .into(binding.ivProfile)

            binding.tvName.displayDataIfNotNull(userDetail.name, null)
            binding.tvLoginName.displayDataIfNotNull(userDetail.login, null)
            binding.tvBio.displayDataIfNotNull(userDetail.bio, null)
            binding.tvCompany.displayDataIfNotNull(userDetail.company, null)
            binding.tvEmail.displayDataIfNotNull(userDetail.email, null)

            binding.tvTwitter.displayDataIfNotNull(
                userDetail.twitterUsername,
                getString(R.string.twitter, userDetail.twitterUsername)
            ) {
                val twitterUrl = getString(R.string.twitter_url, userDetail.twitterUsername)
                try {
                    val url = getString(R.string.twitter_url, userDetail.twitterUsername)
                    val twitterIntent = Intent(Intent.ACTION_VIEW)
                    twitterIntent.data = Uri.parse(url)
                    startActivity(twitterIntent)
                } catch (e: Exception) {
                    Timber.e(e)
                    val message = getString(R.string.no_application_available, twitterUrl)
                    val snackbar =
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }


            binding.tvLocation.displayDataIfNotNull(userDetail.location, null)
            binding.tvBlog.displayDataIfNotNull(userDetail.blog) {
                try {
                    val url = userDetail.blog
                    val blogIntent = Intent(Intent.ACTION_VIEW)
                    blogIntent.data = Uri.parse(url)
                    startActivity(blogIntent)
                } catch (e: Exception) {
                    Timber.e(e)
                    val message =
                        getString(R.string.no_application_available, userDetail.blog)
                    val snackbar =
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }



            binding.tvFollowers.apply {
                show()
                text = getString(R.string.followers_users, userDetail.followers)
                setOnClickListener {
                    val bundle = bundleOf(
                        LOGIN to userDetail.login,
                        IS_FOLLOWING to false,
                        TITLE to getString(R.string.follower)
                    )
                    findNavController()
                        .navigateSafe(
                            R.id.action_userDetailsFragment_to_followFragment,
                            bundle
                        )
                }
            }

            binding.tvFollowing.apply {
                show()
                text = getString(R.string.following_users, userDetail.following)
                setOnClickListener {
                    val bundle = bundleOf(
                        LOGIN to userDetail.login,
                        IS_FOLLOWING to true,
                        TITLE to getString(R.string.following)

                    )
                    findNavController()
                        .navigateSafe(
                            R.id.action_userDetailsFragment_to_followFragment,
                            bundle
                        )
                }
            }

            if (userDetail.publicRepos != null) {
                binding.tvRepos.apply {
                    show()
                    text = getString(R.string.repository_number, userDetail.publicRepos)
                    setOnClickListener {
                        val bundle = bundleOf(LOGIN to userDetail.login)
                        findNavController().navigateSafe(
                            R.id.action_userDetailsFragment_to_githubRepoFragment,
                            bundle
                        )
                    }
                }
            } else {
                binding.tvRepos.hide()
            }
        }else{
            binding.emptyList.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}