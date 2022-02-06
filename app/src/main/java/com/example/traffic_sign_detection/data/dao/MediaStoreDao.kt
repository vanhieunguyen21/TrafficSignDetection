package com.example.traffic_sign_detection.data.dao

import com.example.traffic_sign_detection.data.model.GalleryImage

interface MediaStoreDao {
    fun getAllImages() : List<GalleryImage>
}