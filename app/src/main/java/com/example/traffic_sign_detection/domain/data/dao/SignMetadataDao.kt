package com.example.traffic_sign_detection.domain.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.traffic_sign_detection.domain.data.model.SignMetadata

@Dao
interface SignMetadataDao {
    @Query("SELECT * FROM SignMetadata")
    suspend fun getAll() : List<SignMetadata>
}