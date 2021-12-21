package com.quovantis.photosearch.views.detail

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.quovantis.photosearch.R
import com.quovantis.photosearch.model.Media
import com.quovantis.photosearch.model.PhotoItem
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotoDetailActivityTest {

    companion object {

        private val mockPhotoItem = PhotoItem(
            author = "Sahil Garg",
            description = "Test Description",
            Media(mediaLink = "https://live.staticflickr.com/65535/51760571945_a21ee042fc_m.jpg"),
            title = "Test Title"
        )

        fun getIntent(): Intent {
            val intent =
                Intent(ApplicationProvider.getApplicationContext(), PhotoDetailActivity::class.java)
            intent.apply {
                putExtras(Bundle().apply {
                    putSerializable(PhotoDetailActivity.ARG_PHOTO_DATA, mockPhotoItem)
                })
            }
            return intent
        }
    }

    @get: Rule
    val activityRule = ActivityScenarioRule<PhotoDetailActivity>(getIntent())

    @Test
    fun test_activity_is_visible() {
        onView(withId(R.id.main_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun test_title_is_showing() {
        onView(withId(R.id.textview_title)).check(matches(withText(containsString(mockPhotoItem.title))))
    }

    @Test
    fun test_description_is_showing() {
        onView(withId(R.id.textview_description)).check(
            matches(
                withText(
                    containsString(
                        mockPhotoItem.description
                    )
                )
            )
        )
    }

    @Test
    fun test_author_is_showing() {
        onView(withId(R.id.textview_author)).check(matches(withText(containsString(mockPhotoItem.author))))
    }

    @Test
    fun test_image_is_showing() {
        onView(withId(R.id.imageview_photo)).check(matches(isDisplayed()))
    }
}