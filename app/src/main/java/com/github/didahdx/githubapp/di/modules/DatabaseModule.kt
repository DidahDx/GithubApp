package com.github.didahdx.githubapp.di.modules

import android.app.Application
import androidx.room.Room
import com.github.didahdx.githubapp.data.local.GithubDatabase
import com.github.didahdx.githubapp.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGithubDatabase(application: Application): GithubDatabase {
        return Room.databaseBuilder(
            application, GithubDatabase::class.java,
            GithubDatabase.GITHUB_DATABASE
        ).fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun provideSearchDao(githubDatabase: GithubDatabase): SearchDao {
        return githubDatabase.getSearchDao()
    }

    @Provides
    @Singleton
    fun provideUserDetailDao(githubDatabase: GithubDatabase): UserDetailsDao {
        return githubDatabase.getUserDetailsDao()
    }

    @Provides
    @Singleton
    fun provideRepositoryDao(githubDatabase: GithubDatabase): RepositoryDao {
        return githubDatabase.getRepositoryDao()
    }

    @Provides
    @Singleton
    fun provideFollowerDao(githubDatabase: GithubDatabase): FollowerDao {
        return githubDatabase.getFollowersDao()
    }

    @Provides
    @Singleton
    fun provideFollowingDao(githubDatabase: GithubDatabase): FollowingDao {
        return githubDatabase.getFollowingDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeys(githubDatabase: GithubDatabase): RemoteKeysDao =
        githubDatabase.remoteKeysDao()
}