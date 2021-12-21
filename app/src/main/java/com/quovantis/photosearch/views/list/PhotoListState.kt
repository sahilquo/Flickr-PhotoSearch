package com.quovantis.photosearch.views.list

import com.quovantis.photosearch.model.PhotoItem

sealed class PhotoListState {
    object Loading : PhotoListState()
    object Empty : PhotoListState()
    data class Success(val photos: List<PhotoItem>) : PhotoListState()
    data class Error(val error: String) : PhotoListState()
}
