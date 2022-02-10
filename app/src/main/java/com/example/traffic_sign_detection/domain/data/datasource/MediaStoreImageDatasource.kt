package com.example.traffic_sign_detection.domain.data.datasource

import com.example.traffic_sign_detection.domain.data.model.GalleryImage

interface MediaStoreImageDatasource {
    suspend fun getAllImages() : List<GalleryImage>
}