package com.example.traffic_sign_detection.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class SignMetaData(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "drawable") val drawable: String,
    @ColumnInfo(name = "description") val description: String?
)