package com.example.traffic_sign_detection.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.traffic_sign_detection.data.dao.SignMetaDataDao
import com.example.traffic_sign_detection.data.model.SignMetaData

@Database(entities = [SignMetaData::class], version = 1)
abstract class MetaDataDatabase : RoomDatabase() {
    abstract fun signMetaDataDao() : SignMetaDataDao
}