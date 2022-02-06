package com.example.traffic_sign_detection.data.repository

import com.example.traffic_sign_detection.data.model.GalleryImage

interface MediaStoreImageRepository {
    suspend fun getAllImages() : List<GalleryImage>
}