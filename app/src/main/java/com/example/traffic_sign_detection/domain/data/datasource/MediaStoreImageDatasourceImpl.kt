package com.example.traffic_sign_detection.domain.data.datasource

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.traffic_sign_detection.domain.data.model.GalleryImage

class MediaStoreImageDatasourceImpl(
    val app: Context
) : MediaStoreImageDatasource {

    companion object {
        private const val TAG = "MediaStoreImageDatasourceImpl"
    }

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
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.VOLUME_NAME,
        )

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
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val volumeNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.VOLUME_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val date = cursor.getLong(dateColumn)
                val size = cursor.getLong(sizeColumn)
                val width = cursor.getLong(widthColumn)
                val height = cursor.getLong(heightColumn)
                val bucketId = cursor.getLong(bucketIdColumn)
                var bucketName = cursor.getString(bucketNameColumn)
                val volumeName = cursor.getString(volumeNameColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                if (bucketName == null) bucketName = ""

                images += GalleryImage(
                    id,
                    contentUri,
                    name,
                    date,
                    size,
                    width,
                    height,
                    bucketId,
                    bucketName,
                    volumeName,
                )
            }
        }
        return images.toList()
    }
}