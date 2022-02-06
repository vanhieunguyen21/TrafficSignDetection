package com.example.traffic_sign_detection.data.repository

import com.example.traffic_sign_detection.data.database.MetaDataDatabase
import com.example.traffic_sign_detection.data.model.SignMetaData
import javax.inject.Inject

class MetaDataRepositoryImpl @Inject constructor(
    private val metaDataDb: MetaDataDatabase
) : MetaDataRepository {
    override suspend fun getAllSignMetaData(): List<SignMetaData> =
        metaDataDb.signMetaDataDao().getAll()
}