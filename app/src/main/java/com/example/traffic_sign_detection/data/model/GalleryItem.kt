package com.example.traffic_sign_detection.data.model

import android.net.Uri

interface GalleryItem

data class GalleryImage(
    val id : Long,
    val uri: Uri,
    val name: String,
    val date: Long,
    val size: Long,
) : GalleryItem

data class GalleryTitle(
    val title: String
) : GalleryItem