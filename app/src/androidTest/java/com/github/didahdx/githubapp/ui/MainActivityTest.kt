package com.github.didahdx.githubapp.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest{


    @Test
    fun mainActivityLaunchesWithOutCrashing(){
        ActivityScenario.launch(MainActivity::class.java)
    }
}