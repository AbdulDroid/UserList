package com.test.fairmoney.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.test.fairmoney.R
import com.test.fairmoney.util.RecyclerViewMatcher
import com.test.fairmoney.view.adapter.UserListAdapter
import com.test.fairmoney.view.fragments.UserListFragment
import kotlinx.android.synthetic.main.content_loading.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class UserListFragmentTest {
    private lateinit var scenario: FragmentScenario<UserListFragment>

    //    private lateinit var fragment: UserListFragment
    @Before
    fun setup() {
        scenario = launchFragmentInContainer<UserListFragment>(Bundle.EMPTY, R.style.AppTheme)
        scenario.onFragment {
//            fragment = it
            it.progress.indeterminateDrawable = ColorDrawable(Color.BLUE)
        }
    }


    @Test
    fun test_user_list_item_visibility() {
        onView(withId(R.id.userList)).check(matches(isDisplayed()))
    }

    @Test
    fun test_that_loader_is_not_displayed() {
        onView(withId(R.id.loader))
            .check(matches(not(isDisplayed())))

    }

    @Test
    fun test_user_list_item_item_views() {
        onView(RecyclerViewMatcher(R.id.userList).atPosition(0))
            .check(matches(hasDescendant(withId(R.id.user_name))))
        onView(RecyclerViewMatcher(R.id.userList).atPosition(0))
            .check(matches(hasDescendant(withId(R.id.user_image))))
        onView(RecyclerViewMatcher(R.id.userList).atPosition(0))
            .check(matches(hasDescendant(withContentDescription(R.string.user_image))))
        onView(RecyclerViewMatcher(R.id.userList).atPosition(0))
            .check(matches(hasDescendant(withId(R.id.user_image))))
    }

    @Test
    fun test_user_list_item_content() {
        onView(RecyclerViewMatcher(R.id.userList).atPositionOnView(0, R.id.user_name))
            .check(matches(withText("Mr. Heinz-Georg Fiedler")))
        onView(RecyclerViewMatcher(R.id.userList).atPositionOnView(0, R.id.user_image))
            .check(matches(isDisplayed()))
        onView(RecyclerViewMatcher(R.id.userList).atPositionOnView(1, R.id.user_name))
            .check(matches(withText("Miss. Katie Hughes")))
        onView(withId(R.id.userList)).perform(
            RecyclerViewActions.scrollToPosition<UserListAdapter.ViewHolder>(
                14
            )
        )
        onView(RecyclerViewMatcher(R.id.userList).atPositionOnView(14, R.id.user_email))
            .check(matches(withText("kenneth.carter@example.com")))
        onView(RecyclerViewMatcher(R.id.userList).atPositionOnView(13, R.id.user_email))
            .check(matches(isClickable()))
        onView(RecyclerViewMatcher(R.id.userList).atPositionOnView(13, R.id.user_email))
            .check(matches(isFocusable()))
    }
}