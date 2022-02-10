package com.example.traffic_sign_detection.presentation.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traffic_sign_detection.domain.application.GlobalData
import com.example.traffic_sign_detection.domain.data.model.GalleryImage
import com.example.traffic_sign_detection.domain.data.repository.MediaStoreImageRepository
import com.example.traffic_sign_detection.presentation.adapter.GalleryRecyclerViewAdapter
import com.example.traffic_sign_detection.util.GalleryUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val mediaStoreImageRepository: MediaStoreImageRepository
) : ViewModel() {

    val adapter = GalleryRecyclerViewAdapter(this)

    private val _selectedImage = MutableLiveData<GalleryImage>(null)
    val selectedImage: LiveData<GalleryImage> get() = _selectedImage
    fun setSelectedImage(image: GalleryImage?) {
        _selectedImage.value = image
    }

    init {
        if (GlobalData.galleryItems != null) {
            adapter.addGalleryItems(GlobalData.galleryItems!!)
        } else {
            viewModelScope.launch {
                val images = mediaStoreImageRepository.getAllImages()
                val itemsWithTitles = GalleryUtil.addGalleryTitlesToGalleryImageList(images)
                GlobalData.galleryItems = itemsWithTitles
                launch(Dispatchers.Main) {
                    adapter.addGalleryItems(itemsWithTitles)
                }
            }
        }
    }
}