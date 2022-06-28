package com.github.didahdx.githubapp.ui.searchUsers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.didahdx.githubapp.data.local.enitities.SearchUserEntity
import com.github.didahdx.githubapp.databinding.ItemUserBinding

class SearchUsersAdapter constructor(
    private val onItemClickListener: OnItemClick
) : PagingDataAdapter<SearchUserEntity, SearchUsersAdapter.SearchUsersViewHolder>(
    SearchUsersDiffUtil()
) {

    inner class SearchUsersViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { user -> onItemClickListener.userClicked(user) }
                }
            }
        }

        fun bind(user: SearchUserEntity) {
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .centerCrop()
                .into(binding.profileImage)

            binding.tvName.text = user.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchUsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchUsersViewHolder, position: Int) {
        getItem(position)?.let { user -> holder.bind(user) }
    }
}

interface OnItemClick {
    fun userClicked(user: SearchUserEntity)
}

class SearchUsersDiffUtil : DiffUtil.ItemCallback<SearchUserEntity>() {
    override fun areItemsTheSame(oldItem: SearchUserEntity, newItem: SearchUserEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SearchUserEntity, newItem: SearchUserEntity): Boolean {
        return oldItem == newItem
    }

}