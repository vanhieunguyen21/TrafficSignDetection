package com.example.traffic_sign_detection.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.traffic_sign_detection.data.dao.SignMetadataDao
import com.example.traffic_sign_detection.data.model.SignMetadata

@Database(entities = [SignMetadata::class], version = 1)
abstract class SignMetadataDatabase : RoomDatabase() {
    abstract fun signMetaDataDao() : SignMetadataDao
}