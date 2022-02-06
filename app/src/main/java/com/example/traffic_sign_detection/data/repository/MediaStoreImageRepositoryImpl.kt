package com.example.traffic_sign_detection.data.repository

import com.example.traffic_sign_detection.data.datasource.MediaStoreImageDatasource
import com.example.traffic_sign_detection.data.model.GalleryImage
import javax.inject.Inject

class MediaStoreImageRepositoryImpl @Inject constructor(
    val datasource: MediaStoreImageDatasource
) : MediaStoreImageRepository {
    override suspend fun getAllImages(): List<GalleryImage> {
        return datasource.getAllImages()
    }

}