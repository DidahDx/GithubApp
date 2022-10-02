package com.github.didahdx.githubapp.ui.searchUsers

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import com.github.didahdx.githubapp.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Before
    fun init() {
        hiltRule.inject()
    }


    @Test
    fun searchUser() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val arg =Bundle()
        val scenario = launchFragmentInHiltContainer<SearchFragment>(arg)

        scenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)

            // Make the NavController available via the findNavController() APIs
            Navigation.setViewNavController(fragment.requireView(), navController)

        }

    }


}