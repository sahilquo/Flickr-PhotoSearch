package com.quovantis.photosearch

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.quovantis.photosearch.db.PSDatabase
import com.quovantis.photosearch.db.dao.RecentSearchDao
import com.quovantis.photosearch.db.entities.RecentSearchData
import junit.framework.TestCase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PSDatabaseTest : TestCase() {

    private lateinit var recentSearchDao: RecentSearchDao
    private lateinit var database: PSDatabase

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, PSDatabase::class.java).build()
        recentSearchDao = database.getRecentSearchDao()
    }

    @Test
    fun test_insert() {
        val searchTerm = "Wallpaper"
        runBlocking {
            recentSearchDao.insertSearch(RecentSearchData(searchTerm))
            val searchList = recentSearchDao.getRecentSearches()
            assertEquals(searchList.firstOrNull()?.get(0)?.value, searchTerm)
        }
    }

    @Test
    fun test_max_5_value_save() {
        val searchTerms = listOf("Wallpaper", "Paint", "Android", "Artist", "Travel", "Mobile")
        runBlocking {
            searchTerms.forEach {
                recentSearchDao.insertSearch(RecentSearchData(it))
            }
            val searchList = recentSearchDao.getRecentSearches()
            assertEquals(searchList.firstOrNull()?.size, 5)
        }
    }

    @Test
    fun test_latest_search_on_top() {
        val searchTerms = listOf("Wallpaper", "Paint", "Android", "Artist", "Travel", "Mobile")
        runBlocking {
            searchTerms.forEach {
                recentSearchDao.insertSearch(RecentSearchData(it))
            }
            val searchList = recentSearchDao.getRecentSearches()
            assertEquals(searchList.firstOrNull()?.get(0)?.value, "Mobile")
        }
    }

    @Test
    fun test_old_search_removed() {
        val searchTerms = listOf("Wallpaper", "Paint", "Android", "Artist", "Travel", "Mobile")
        runBlocking {
            searchTerms.forEach {
                recentSearchDao.insertSearch(RecentSearchData(it))
            }
            val searchList = recentSearchDao.getRecentSearches().firstOrNull()
            assertTrue(searchList?.stream()?.anyMatch { it.value != "Wallpaper" } ?: false)
        }
    }

    @After
    fun closeDatabase() {
        database.close()
    }
}