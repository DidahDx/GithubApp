package com.github.didahdx.githubapp.ui.githubRepository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.didahdx.githubapp.R
import com.github.didahdx.githubapp.common.extension.displayDataIfNotNull
import com.github.didahdx.githubapp.common.extension.hide
import com.github.didahdx.githubapp.common.extension.setVisibility
import com.github.didahdx.githubapp.common.extension.show
import com.github.didahdx.githubapp.data.remote.dto.RepositoryDto
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.databinding.ItemGithubRepositoryBinding
import com.google.android.material.chip.Chip

class RepoAdapter(
    private val onItemClickListener: OnItemClickListener
) : PagingDataAdapter<RepositoryDto, RepoAdapter.RepoViewHolder>(RepoDiffUtil()) {

    inner class RepoViewHolder(private val binding: ItemGithubRepositoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { repositoryDto -> onItemClickListener.onClick(repositoryDto) }
                }
            }
        }
        fun bind(repositoryDto: RepositoryDto) {
            binding.tvArchived.setVisibility(repositoryDto.archived)
            binding.tvName.displayDataIfNotNull(repositoryDto.name, null)
            binding.tvDefaultBranch.displayDataIfNotNull(repositoryDto.defaultBranch, null)
            binding.tvDescription.displayDataIfNotNull(repositoryDto.description, null)
            binding.tvLicense.displayDataIfNotNull(repositoryDto.license?.name, null)
            binding.tvLicense.isSelected = true
            binding.tvForked.setVisibility(repositoryDto.fork)
            binding.tvWatchers.displayDataIfNotNull(
                repositoryDto.watchersCount.toString(),
                binding.root.context.getString(
                    R.string.watching_number,
                    repositoryDto.watchersCount
                ),
                null
            )
            binding.tvLanguage.displayDataIfNotNull(repositoryDto.language, null)
            binding.tvStars.displayDataIfNotNull(repositoryDto.stargazersCount.toString(), null)
            binding.tvIssues.displayDataIfNotNull(
                repositoryDto.openIssuesCount.toString(),
                binding.root.context.getString(
                    R.string.issues_number,
                    repositoryDto.openIssuesCount
                ), null
            )

            binding.tvFork.displayDataIfNotNull(
                repositoryDto.forksCount.toString(),
                binding.root.context.getString(R.string.forks_number, repositoryDto.forksCount),
                null
            )

            binding.chipGroupTopic.removeAllViews()
            val topics = repositoryDto.topics
            if (!topics.isNullOrEmpty()) {
                binding.hsTopic.show()
                for (item in topics.indices) {
                    val chip = Chip(binding.chipGroupTopic.context)
                    val topicTextChip = topics[item]
                    chip.text = topicTextChip
                    chip.id = item
                    binding.chipGroupTopic.addView(chip)
                }
            } else {
                binding.hsTopic.hide()
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding =
            ItemGithubRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

interface OnItemClickListener {
    fun onClick(repositoryDto: RepositoryDto)
}

class RepoDiffUtil : DiffUtil.ItemCallback<RepositoryDto>() {
    override fun areItemsTheSame(oldItem: RepositoryDto, newItem: RepositoryDto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RepositoryDto, newItem: RepositoryDto): Boolean {
        return oldItem == newItem
    }

}