package com.github.didahdx.githubapp.ui.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.didahdx.githubapp.data.model.User
import com.github.didahdx.githubapp.databinding.ItemUserBinding

class UserAdapter constructor(
    private val onItemClickListener: OnItemClick
) : PagingDataAdapter<User, UserAdapter.FollowsViewHolder>(UserDiffUtil()) {

    inner class FollowsViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { follows ->
                        onItemClickListener.userClicked(
                            follows
                        )
                    }
                }
            }
        }

        fun bind(follows: User) {
            Glide.with(binding.root.context)
                .load(follows.avatarUrl)
                .centerCrop()
                .into(binding.profileImage)

            binding.tvName.text = follows.login
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowsViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowsViewHolder, position: Int) {
        getItem(position)?.let { follows -> holder.bind(follows) }
    }
}

interface OnItemClick {
    fun userClicked(user: User)
}

class UserDiffUtil : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}