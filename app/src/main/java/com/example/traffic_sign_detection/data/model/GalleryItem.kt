package com.example.traffic_sign_detection.data.model

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi

interface GalleryItem

data class GalleryImage(
    val id: Long,
    val uri: Uri,
    val name: String,
    val date: Long,
    val size: Long,
    val width: Long?,
    val height: Long?,

    // For API < 29 {
    val data: String?,
    // For API < 29 }

    // For API >= 29 {
    val relativePath: String?,
    // For API >= 29 }
) : GalleryItem

data class GalleryTitle(
    val title: String
) : GalleryItem