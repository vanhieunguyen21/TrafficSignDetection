package com.example.traffic_sign_detection.data.repository

import com.example.traffic_sign_detection.data.datasource.SignMetadataDatabase
import com.example.traffic_sign_detection.data.model.SignMetadata
import javax.inject.Inject

class SignMetadataRepositoryImpl @Inject constructor(
    private val signMetadataDb: SignMetadataDatabase
) : SignMetadataRepository {
    override suspend fun getAllSignMetadata(): List<SignMetadata> =
        signMetadataDb.signMetaDataDao().getAll()
}