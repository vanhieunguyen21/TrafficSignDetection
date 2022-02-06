package com.example.traffic_sign_detection.data.repository

import com.example.traffic_sign_detection.data.model.SignMetaData

interface MetaDataRepository {
    suspend fun getAllSignMetaData() : List<SignMetaData>
}