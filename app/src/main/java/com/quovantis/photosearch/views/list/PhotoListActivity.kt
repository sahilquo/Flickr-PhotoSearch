package com.quovantis.photosearch.views.list

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.quovantis.photosearch.PhotoSearchViewModel
import com.quovantis.photosearch.databinding.ActivityPhotoListBinding
import com.quovantis.photosearch.utils.ViewUtils
import com.quovantis.photosearch.utils.getValueChangeStateFlow
import com.quovantis.photosearch.views.detail.PhotoDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@AndroidEntryPoint
class PhotoListActivity : AppCompatActivity() {

    private val viewModel: PhotoSearchViewModel by viewModels()

    private lateinit var binding: ActivityPhotoListBinding

    private lateinit var photosAdapter: PhotoListAdapter
    private lateinit var recentSearchAdapter: RecentSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()
        observeData()
    }

    private fun initializeUI() {
        with(binding) {
            imageviewSearch.setOnClickListener { ViewUtils.hideKeyboard(this@PhotoListActivity) }

            // Setup Photos Adapter
            photosAdapter = PhotoListAdapter()
            recyclerviewPhotos.apply {
                adapter = photosAdapter
            }

            photosAdapter.setOnItemClickListener {
                PhotoDetailActivity.start(this@PhotoListActivity, it)
            }

            // Setup Search Term Adapter
            recentSearchAdapter = RecentSearchAdapter()
            recyclerviewSearches.apply {
                adapter = recentSearchAdapter
            }

            recentSearchAdapter.setOnItemClickListener {
                edittextSearch.setText(it.value)
                edittextSearch.setSelection(edittextSearch.length())
            }

            toggleViews(viewModel.currentSearchTerm.value ?: "")
            initListener()
        }
    }

    private fun initListener() {
        lifecycleScope.launchWhenStarted {
            viewModel.getRecentSearches()

            binding.edittextSearch.getValueChangeStateFlow()
                .debounce(500L)
                .filter { query ->
                    return@filter if (query == null) false else {
                        toggleViews(query)
                        true
                    }
                }
                .distinctUntilChanged()
                .collect { searchTerm ->
                    if (!searchTerm.isNullOrEmpty())
                        viewModel.searchPhotos(searchTerm)
                    else if (searchTerm?.isEmpty() == true) {
                        viewModel.updateCurrentTerm(searchTerm)
                    }
                }
        }
    }

    private fun toggleViews(searchTerm: String) {
        if (searchTerm.isEmpty()) {
            showSearchList()
            photosAdapter.differ.submitList(emptyList())
        } else {
            hideSearchList()
        }
    }

    private fun showSearchList() {
        binding.groupRecentSearch.isVisible = true
        binding.recyclerviewPhotos.isVisible = false
    }

    private fun hideSearchList() {
        binding.groupRecentSearch.isVisible = false
        binding.textviewSearchCaption.isVisible = false
        binding.recyclerviewPhotos.isVisible = true
    }

    private fun observeData() = lifecycleScope.launchWhenStarted {
        viewModel.photoListData.observe(this@PhotoListActivity, {
            when (it) {
                is PhotoListState.Loading -> {
                    binding.progressbar.isVisible = true
                }
                is PhotoListState.Success -> {
                    binding.progressbar.isVisible = false
                    photosAdapter.differ.submitList(it.photos)
                }
                is PhotoListState.Error -> {
                    binding.progressbar.isVisible = false
                    ViewUtils.showShortToast(this@PhotoListActivity, it.error)
                }
                is PhotoListState.Empty -> {
                    binding.progressbar.isVisible = false
                }
            }
        })

        viewModel.searchListData.collect {
            if (binding.groupRecentSearch.isVisible) {
                binding.textviewSearchCaption.isVisible = it.isEmpty()
            }
            recentSearchAdapter.differ.submitList(it)
        }
    }
}