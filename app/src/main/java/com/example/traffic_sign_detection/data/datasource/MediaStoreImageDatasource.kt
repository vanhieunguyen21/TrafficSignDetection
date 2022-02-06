package com.example.traffic_sign_detection.data.datasource

import com.example.traffic_sign_detection.data.model.GalleryImage

interface MediaStoreImageDatasource {
    suspend fun getAllImages() : List<GalleryImage>
}