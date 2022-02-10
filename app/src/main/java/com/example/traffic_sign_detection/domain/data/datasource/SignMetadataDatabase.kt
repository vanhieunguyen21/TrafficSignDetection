package com.example.traffic_sign_detection.domain.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.traffic_sign_detection.domain.data.dao.SignMetadataDao
import com.example.traffic_sign_detection.domain.data.model.SignMetadata

@Database(entities = [SignMetadata::class], version = 1)
abstract class SignMetadataDatabase : RoomDatabase() {
    abstract fun signMetaDataDao() : SignMetadataDao
}