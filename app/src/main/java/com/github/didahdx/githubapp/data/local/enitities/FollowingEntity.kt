package com.github.didahdx.githubapp.data.local.enitities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.didahdx.githubapp.data.model.User

@Entity
data class FollowingEntity(
    val avatarUrl: String,
    val htmlUrl: String,
    val id: Int,
    val login: String,
    var username: String?
) {
    @PrimaryKey(autoGenerate = true)
    var idLocal: Long = 0

    fun mapToUser(): User {
        return User(avatarUrl, htmlUrl, id, login, username)
    }
}
