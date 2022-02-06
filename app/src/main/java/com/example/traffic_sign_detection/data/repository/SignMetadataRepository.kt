package com.example.traffic_sign_detection.data.repository

import com.example.traffic_sign_detection.data.model.SignMetadata

interface SignMetadataRepository {
    suspend fun getAllSignMetadata() : List<SignMetadata>
}