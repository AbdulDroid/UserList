package com.user.list.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.user.list.R
import com.user.list.util.getUserWithData
import com.user.list.view.fragments.UserDetailFragment
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDetailFragmentTest {

    @Before
    fun setup() {
        val args = bundleOf("user" to getUserWithData())
        launchFragmentInContainer<UserDetailFragment>(
            fragmentArgs = args,
            themeResId = R.style.AppTheme
        ).onFragment {
            it.binding.container.progress.indeterminateDrawable = ColorDrawable(Color.BLUE)
        }
    }

    @Test
    fun test_user_detail_fragment_views() {
        onView(withId(R.id.back_button)).check(matches(isClickable()))
        onView(withId(R.id.user_image)).check(matches(withContentDescription(R.string.user_image)))
        onView(withId(R.id.user_name)).check(matches(isDisplayed()))
        onView(withId(R.id.user_email)).check(matches(isDisplayed()))
        onView(withId(R.id.detailGroup)).check(matches(not(isDisplayed())))
    }

    @Test
    fun test_user_detail_starting_content() {
        onView(withId(R.id.user_name)).check(matches(withText("Mr. Heinz-Georg Fiedler")))
        onView(withId(R.id.user_email)).check(matches(withText("heinz-georg.fiedler@example.com")))
        onView(withId(R.id.user_email)).check(matches(isClickable()))
        onView(withId(R.id.user_email)).check(matches(isFocusable()))
    }
}