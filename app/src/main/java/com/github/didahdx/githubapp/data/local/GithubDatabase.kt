package com.github.didahdx.githubapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.didahdx.githubapp.data.local.dao.*
import com.github.didahdx.githubapp.data.local.enitities.*

@Database(
    entities = [SearchUserEntity::class, UserDetailsEntity::class, RepositoryEntity::class, RemoteKeys::class,
        FollowingEntity::class, FollowersEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TopicsConvertor::class)
abstract class GithubDatabase : RoomDatabase() {

    abstract fun getSearchDao(): SearchDao
    abstract fun getUserDetailsDao(): UserDetailsDao
    abstract fun getRepositoryDao(): RepositoryDao
    abstract fun getFollowingDao(): FollowingDao
    abstract fun getFollowersDao(): FollowerDao
    abstract fun remoteKeysDao(): RemoteKeysDao


    companion object {
        const val GITHUB_DATABASE = "githubDatabase"
    }
}