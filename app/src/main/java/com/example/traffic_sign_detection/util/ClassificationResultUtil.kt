package com.example.traffic_sign_detection.util

import com.example.traffic_sign_detection.domain.data.model.Prediction

object ClassificationResultUtil {
    fun filterHighAccuracyPrediction(predictions: List<Prediction>): List<Prediction> =
        predictions.filter {
            it.accuracy > 0.2f
        }

}