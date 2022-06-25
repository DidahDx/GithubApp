package com.github.didahdx.githubapp.ui.searchUsers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.didahdx.githubapp.data.remote.dto.User
import com.github.didahdx.githubapp.databinding.ItemUserBinding

class UsersAdapter constructor(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<User, UsersAdapter.UsersViewHolder>(UsersDiffUtil()) {

    inner class UsersViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.userClicked(getItem(position))
                }
            }
        }

        fun bind(user: User) {
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .centerCrop()
                .into(binding.profileImage)

            binding.tvName.text = user.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

interface OnItemClickListener {
    fun userClicked(user: User)
}

class UsersDiffUtil : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}