package com.example.traffic_sign_detection.domain.application

import com.example.traffic_sign_detection.domain.data.model.GalleryItem
import com.example.traffic_sign_detection.domain.data.model.SignMetadata

object GlobalData {
    val metaDataMap = HashMap<Int, SignMetadata>()
    var galleryItems : List<GalleryItem>? = null
}