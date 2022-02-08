package com.example.traffic_sign_detection.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.data.model.GalleryImage
import com.example.traffic_sign_detection.data.model.GalleryItem
import com.example.traffic_sign_detection.presentation.dataBinder.DataBindAdapter
import com.example.traffic_sign_detection.presentation.dataBinder.DataBinder
import com.example.traffic_sign_detection.presentation.dataBinder.GalleryImageViewBinder
import com.example.traffic_sign_detection.presentation.dataBinder.GalleryTitleViewBinder
import com.example.traffic_sign_detection.util.GalleryUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryRecyclerViewAdapter :
    DataBindAdapter<GalleryItem>() {

    companion object {
        const val TITLE_VIEW_TYPE = 0
        const val IMAGE_VIEW_TYPE = 1
    }

    private val galleryItems: MutableList<GalleryItem> = mutableListOf()
    private val imageViewBinder = GalleryImageViewBinder(this)
    private val titleViewBinder = GalleryTitleViewBinder(this)

    fun addGalleryItems(items: List<GalleryImage>) {
        galleryItems.clear()
        CoroutineScope(Dispatchers.Default).launch {
            val itemsWithTitles = GalleryUtil.addGalleryTitlesToGalleryImageList(items)
            galleryItems.addAll(itemsWithTitles)
            launch(Dispatchers.Main) {
                notifyItemRangeChanged(0, itemsWithTitles.size)
            }
        }
    }

    override fun getItemCount(): Int {
        return galleryItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (galleryItems[position] is GalleryImage) IMAGE_VIEW_TYPE else TITLE_VIEW_TYPE
    }

    override fun getItem(position: Int): GalleryItem {
        return galleryItems[position]
    }

    override fun getDataBinder(viewType: Int): DataBinder<RecyclerView.ViewHolder, GalleryItem> {
        return if (viewType == IMAGE_VIEW_TYPE) imageViewBinder else titleViewBinder
    }
}