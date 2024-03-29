/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.chuckyfacts

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule

import com.raywenderlich.chuckyfacts.view.activities.MainActivity
import com.raywenderlich.chuckyfacts.view.adapters.JokesListAdapter

import org.junit.Rule
import org.junit.Test

class MainActivityInstrumentedTest {

  @Rule
  @JvmField
  val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

  @Test
  fun testRecyclerViewIsPopulated() {

    waitForSplashScreen()

    onView(withId(R.id.rv_jokes_list_activity_main))
        .check(matches(hasDescendant(withText("2"))))
  }

  @Test
  fun testRecyclerViewItemClickLaunchesDetailActivity() {

    waitForSplashScreen()

    onView(withId(R.id.rv_jokes_list_activity_main))
        .perform(RecyclerViewActions.scrollToPosition<JokesListAdapter.ViewHolder>(2))
        .perform(RecyclerViewActions.actionOnItemAtPosition<JokesListAdapter.ViewHolder>(2, click()))

    assert(onView(ViewMatchers.withId(R.id.rv_jokes_list_activity_main)) == null)
  }
}
