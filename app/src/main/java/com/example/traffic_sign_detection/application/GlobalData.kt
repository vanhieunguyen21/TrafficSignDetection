package com.example.traffic_sign_detection.application

import com.example.traffic_sign_detection.data.model.GalleryItem
import com.example.traffic_sign_detection.data.model.SignMetadata

object GlobalData {
    val metaDataMap = HashMap<Int, SignMetadata>()
    var galleryItems : List<GalleryItem>? = null
}