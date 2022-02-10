package com.example.traffic_sign_detection.domain.data.repository

import com.example.traffic_sign_detection.domain.data.datasource.MediaStoreImageDatasource
import com.example.traffic_sign_detection.domain.data.model.GalleryImage
import javax.inject.Inject

class MediaStoreImageRepositoryImpl @Inject constructor(
    val datasource: MediaStoreImageDatasource
) : MediaStoreImageRepository {
    override suspend fun getAllImages(): List<GalleryImage> {
        return datasource.getAllImages()
    }

}