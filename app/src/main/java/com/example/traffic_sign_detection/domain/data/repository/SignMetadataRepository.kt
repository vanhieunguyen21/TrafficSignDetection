package com.example.traffic_sign_detection.domain.data.repository

import com.example.traffic_sign_detection.domain.data.model.SignMetadata

interface SignMetadataRepository {
    suspend fun getAllSignMetadata() : List<SignMetadata>
}