package com.example.traffic_sign_detection.presentation.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traffic_sign_detection.data.repository.MediaStoreImageRepository
import com.example.traffic_sign_detection.presentation.adapter.GalleryRecyclerViewAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val mediaStoreImageRepository: MediaStoreImageRepository
): ViewModel(){
    val adapter = GalleryRecyclerViewAdapter()

    init {
        viewModelScope.launch {
            val images = mediaStoreImageRepository.getAllImages()
            adapter.addGalleryItems(images)
        }
    }
}