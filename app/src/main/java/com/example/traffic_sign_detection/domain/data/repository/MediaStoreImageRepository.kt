package com.example.traffic_sign_detection.domain.data.repository

import com.example.traffic_sign_detection.domain.data.model.GalleryImage

interface MediaStoreImageRepository {
    suspend fun getAllImages() : List<GalleryImage>
}