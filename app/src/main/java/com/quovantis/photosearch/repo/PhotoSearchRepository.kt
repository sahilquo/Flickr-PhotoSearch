package com.quovantis.photosearch.repo

import com.quovantis.photosearch.db.dao.RecentSearchDao
import com.quovantis.photosearch.db.entities.RecentSearchData
import com.quovantis.photosearch.model.PhotoResponse
import com.quovantis.photosearch.network.PhotoSearchApi
import javax.inject.Inject

class PhotoSearchRepository @Inject constructor(
    private val api: PhotoSearchApi,
    private val recentSearchDao: RecentSearchDao
) {

    suspend fun search(tags: String): PhotoResponse? {
        return api.search(tags)
    }

    suspend fun insertSearch(searchItem: RecentSearchData) {
        recentSearchDao.insertSearch(searchItem)
        removeOldSearches()
    }

    fun getRecentSearches() = recentSearchDao.getRecentSearches()

    private suspend fun removeOldSearches() = recentSearchDao.removeOldSearches()

}