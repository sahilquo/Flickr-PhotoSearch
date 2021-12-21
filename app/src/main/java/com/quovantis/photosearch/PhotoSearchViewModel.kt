package com.quovantis.photosearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quovantis.photosearch.db.entities.RecentSearchData
import com.quovantis.photosearch.repo.PhotoSearchRepository
import com.quovantis.photosearch.views.list.PhotoListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class PhotoSearchViewModel @Inject constructor(
    private val repository: PhotoSearchRepository
) : ViewModel() {

    private val _photoListData = MutableLiveData<PhotoListState>(PhotoListState.Empty)
    private val _searchListData = MutableStateFlow<List<RecentSearchData>>(emptyList())

    val photoListData: LiveData<PhotoListState> = _photoListData
    val searchListData: StateFlow<List<RecentSearchData>> = _searchListData

    private var _currentSearchTerm = MutableLiveData("")
    val currentSearchTerm: LiveData<String> = _currentSearchTerm

    fun searchPhotos(searchTerm: String) {
        viewModelScope.launch {
            _photoListData.value = PhotoListState.Loading
            val tags = searchTerm.trim().split(' ').joinToString(separator = ",")
            val response = repository.search(tags)

            if (response != null) {
                if (response.items.isNullOrEmpty()) {
                    _photoListData.value = PhotoListState.Empty
                } else {
                    insertSearch(searchTerm)
                    _photoListData.value = PhotoListState.Success(response.items)
                }
            } else {
                _photoListData.value = PhotoListState.Error("Data not found")
            }
        }
    }

    private fun insertSearch(searchTerm: String) {
        updateCurrentTerm(searchTerm)
        viewModelScope.launch {
            repository.insertSearch(RecentSearchData(value = searchTerm))
        }
    }

    fun getRecentSearches() {
        viewModelScope.launch {
            repository.getRecentSearches().collect { result ->
                if (result.isNullOrEmpty()) {
                    _searchListData.value = emptyList()
                } else {
                    _searchListData.value = result
                }
            }
        }
    }

    fun updateCurrentTerm(searchTerm: String) {
        _currentSearchTerm.postValue(searchTerm)
    }
}