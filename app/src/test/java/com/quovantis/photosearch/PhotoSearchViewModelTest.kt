package com.quovantis.photosearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.quovantis.photosearch.db.dao.RecentSearchDao
import com.quovantis.photosearch.model.PhotoItem
import com.quovantis.photosearch.model.PhotoResponse
import com.quovantis.photosearch.network.PhotoSearchApi
import com.quovantis.photosearch.repo.PhotoSearchRepository
import com.quovantis.photosearch.views.list.PhotoListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PhotoSearchViewModelTest {

    @get: Rule
    val instantExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var api: PhotoSearchApi

    @Mock
    private lateinit var recentSearchDao: RecentSearchDao

    private lateinit var repository: PhotoSearchRepository

    private lateinit var viewModel: PhotoSearchViewModel

    @Mock
    private lateinit var photoObserver: Observer<PhotoListState>

    private val mockPhotoList = ArrayList<PhotoItem>()

    @Before
    fun setUp() {
        repository = PhotoSearchRepository(api, recentSearchDao)
        viewModel = PhotoSearchViewModel(repository)

        mockPhotoList.add(Mockito.mock(PhotoItem::class.java))
        mockPhotoList.add(Mockito.mock(PhotoItem::class.java))
        mockPhotoList.add(Mockito.mock(PhotoItem::class.java))
        mockPhotoList.add(Mockito.mock(PhotoItem::class.java))
        mockPhotoList.add(Mockito.mock(PhotoItem::class.java))
    }

    @Test
    fun `Test search photo api return empty list`() {
        testCoroutineRule.runBlockingTest {
            Mockito.doReturn(PhotoResponse(emptyList()))
                .`when`(api)
                .search("Wallpaper")
            viewModel.photoListData.observeForever(photoObserver)
            viewModel.searchPhotos("Wallpaper")
            Mockito.verify(photoObserver, Mockito.atLeast(1)).onChanged(PhotoListState.Empty)
            viewModel.photoListData.removeObserver(photoObserver)
        }
    }

    @Test
    fun `Test search photo api return some data`() {
        testCoroutineRule.runBlockingTest {
            Mockito.doReturn(PhotoResponse(mockPhotoList))
                .`when`(api)
                .search("Wallpaper")
            viewModel.photoListData.observeForever(photoObserver)
            viewModel.searchPhotos("Wallpaper")
            Mockito.verify(photoObserver, Mockito.atLeast(1))
                .onChanged(PhotoListState.Success(mockPhotoList))
            viewModel.photoListData.removeObserver(photoObserver)
        }
    }

    @Test
    fun `Test search photo api return error`() {
        testCoroutineRule.runBlockingTest {
            Mockito.doReturn(null)
                .`when`(api)
                .search("Wallpaper")
            viewModel.photoListData.observeForever(photoObserver)
            viewModel.searchPhotos("Wallpaper")
            Mockito.verify(photoObserver, Mockito.atLeast(1))
                .onChanged(PhotoListState.Error("Data not found"))
            viewModel.photoListData.removeObserver(photoObserver)
        }
    }
}