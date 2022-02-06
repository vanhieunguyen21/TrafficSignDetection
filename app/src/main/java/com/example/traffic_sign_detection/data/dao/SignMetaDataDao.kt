package com.example.traffic_sign_detection.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.traffic_sign_detection.data.model.SignMetaData

@Dao
interface SignMetaDataDao {
    @Query("SELECT * FROM SignMetaData")
    suspend fun getAll() : List<SignMetaData>
}