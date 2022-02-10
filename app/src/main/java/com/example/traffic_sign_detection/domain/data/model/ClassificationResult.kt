package com.example.traffic_sign_detection.domain.data.model

import android.graphics.Bitmap
import java.io.Serializable

data class ClassificationResult(
    val bmp: Bitmap,
    val predictions: List<Prediction>
) : Serializable

class Prediction(
    val accuracy: Float,
    id: Int,
    label: String,
    name: String,
    drawable: String,
    description: String?
) : SignMetadata(id, label, name, drawable, description)
