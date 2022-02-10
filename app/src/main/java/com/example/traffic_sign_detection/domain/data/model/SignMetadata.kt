package com.example.traffic_sign_detection.domain.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
open class SignMetadata(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "drawable") val drawable: String,
    @ColumnInfo(name = "description") val description: String?,
) : Serializable {
    @Ignore
    var drawableResourceId: Int = 0

    constructor(
        id: Int,
        label: String,
        name: String,
        drawable: String,
        description: String?,
        drawableResourceId: Int
    ) : this(id, label, name, drawable, description) {
        this.drawableResourceId = drawableResourceId
    }
}