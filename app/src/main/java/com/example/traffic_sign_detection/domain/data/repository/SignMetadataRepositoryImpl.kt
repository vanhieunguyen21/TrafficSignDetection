package com.example.traffic_sign_detection.domain.data.repository

import com.example.traffic_sign_detection.domain.data.datasource.SignMetadataDatabase
import com.example.traffic_sign_detection.domain.data.model.SignMetadata
import javax.inject.Inject

class SignMetadataRepositoryImpl @Inject constructor(
    private val signMetadataDb: SignMetadataDatabase
) : SignMetadataRepository {
    override suspend fun getAllSignMetadata(): List<SignMetadata> =
        signMetadataDb.signMetaDataDao().getAll()
}