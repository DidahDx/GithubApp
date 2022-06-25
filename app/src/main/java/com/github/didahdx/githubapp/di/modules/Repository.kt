package com.github.didahdx.githubapp.di.modules

import com.github.didahdx.githubapp.data.repository.GithubUsersRepository
import com.github.didahdx.githubapp.data.repository.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Repository {

    @Binds
    @Singleton
    abstract fun bindGithubRepository(githubUsersRepository: GithubUsersRepository): UsersRepository
}