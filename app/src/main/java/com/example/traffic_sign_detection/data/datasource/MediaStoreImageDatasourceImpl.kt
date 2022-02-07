package com.example.traffic_sign_detection.data.datasource

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.traffic_sign_detection.application.BaseApplication
import com.example.traffic_sign_detection.data.model.GalleryImage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaStoreImageDatasourceImpl(
    val app: Context
) : MediaStoreImageDatasource {

    @SuppressLint("InlinedApi")
    override suspend fun getAllImages(): List<GalleryImage> {
        val belowQ = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        val images = mutableListOf<GalleryImage>()
        val collection =
            if (belowQ) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)

        val projection = mutableListOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
        )
        if (belowQ) {
            projection.add(MediaStore.Images.Media.DATA)
        } else {
            projection.add(MediaStore.Images.Media.RELATIVE_PATH)
        }

        val selection = null
        val selectionArgs = null
        val sortOrder = null
        val query = app.contentResolver.query(
            collection,
            projection.toTypedArray(),
            selection,
            selectionArgs,
            sortOrder
        )
        query?.use { cursor ->
            // Cache column indices
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
            val dataColumn =
                if (belowQ) cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) else null
            val pathColumn =
                if (belowQ) null else cursor.getColumnIndexOrThrow(MediaStore.Images.Media.RELATIVE_PATH)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val date = cursor.getLong(dateColumn)
                val size = cursor.getLong(sizeColumn)
                val width = cursor.getLong(widthColumn)
                val height = cursor.getLong(heightColumn)
                val data = if (belowQ) cursor.getString(dataColumn!!) else null
                val path = if (belowQ) null else cursor.getString(pathColumn!!)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                images += if (belowQ) GalleryImage(
                    id,
                    contentUri,
                    name,
                    date,
                    size,
                    width,
                    height,
                    data = data,
                    relativePath = null
                ) else GalleryImage(
                    id,
                    contentUri,
                    name,
                    date,
                    size,
                    width,
                    height,
                    data = null,
                    relativePath = path
                )
            }
        }
        return images.toList()
    }
}