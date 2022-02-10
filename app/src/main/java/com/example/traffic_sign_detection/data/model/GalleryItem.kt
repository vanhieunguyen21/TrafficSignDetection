package com.example.traffic_sign_detection.data.model

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable

interface GalleryItem

data class GalleryImage(
    val id: Long,
    val uri: Uri,
    val name: String,
    val addedTimestamp: Long,
    val size: Long,
    val width: Long,
    val height: Long,
    val bucketId: Long,
    val bucketName: String,
    val volumeName: String
) : GalleryItem, Serializable

data class GalleryTitle(
    val bucketId: Long,
    val bucketName: String,
    val volumeName: String,
) : GalleryItem