package com.example.traffic_sign_detection.presentation.ui.gallery

import androidx.lifecycle.ViewModel
import com.example.traffic_sign_detection.presentation.adapter.GalleryRecyclerViewAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(

): ViewModel(){
    val adapter = GalleryRecyclerViewAdapter()

    init {

    }
}