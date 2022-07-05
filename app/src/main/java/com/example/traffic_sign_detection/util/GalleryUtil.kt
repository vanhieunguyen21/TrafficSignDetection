package com.example.traffic_sign_detection.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.traffic_sign_detection.domain.data.model.GalleryImage
import com.example.traffic_sign_detection.domain.data.model.GalleryItem
import com.example.traffic_sign_detection.domain.data.model.GalleryTitle

object GalleryUtil {
    private data class Bucket(
        val id: Long,
        val name: String,
        val volumeName: String,
        var lastAddedTimestamp: Long,
        val images: MutableList<GalleryImage>
    )

    suspend fun addGalleryTitlesToGalleryImageList(images: List<GalleryImage>): List<GalleryItem> {
        val result = mutableListOf<GalleryItem>()

        val bucketMap = HashMap<Long, Bucket>()
        images.forEach { image ->
            val bucketId = image.bucketId
            if (bucketMap[bucketId] == null) {
                bucketMap[bucketId] =
                    Bucket(
                        bucketId,
                        image.bucketName,
                        image.volumeName,
                        0,
                        mutableListOf()
                    )
            }
            bucketMap[bucketId]!!.images += image
            if (image.addedTimestamp > bucketMap[bucketId]!!.lastAddedTimestamp) {
                bucketMap[bucketId]!!.lastAddedTimestamp = image.addedTimestamp
            }
        }

        val orderedBucketList =
            bucketMap.toList()
                .map { it.second }
                .sortedBy { -it.lastAddedTimestamp }

        orderedBucketList.forEach { bucket ->
            result += GalleryTitle(bucket.id, bucket.name, bucket.volumeName)
            result.addAll(bucket.images.sortedBy { -it.addedTimestamp })
        }

        return result.toList()
    }

    fun getBitmap(contentResolver: ContentResolver, fileUri: Uri?): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri!!))
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
            }
        } catch (e: Exception){
            null
        }
    }
}