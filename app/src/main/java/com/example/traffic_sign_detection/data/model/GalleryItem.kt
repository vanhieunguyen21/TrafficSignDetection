package com.example.traffic_sign_detection.data.model

interface GalleryItem

data class GalleryImage(
    val id : Int
) : GalleryItem

data class GalleryTitle(
    val title: String
) : GalleryItem