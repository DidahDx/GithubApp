package com.github.didahdx.githubapp

import android.app.Application
import com.github.didahdx.githubapp.common.TimberLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var timberLoggingTree: TimberLoggingTree

    override fun onCreate() {
        super.onCreate()
        Timber.plant(timberLoggingTree)
    }
}