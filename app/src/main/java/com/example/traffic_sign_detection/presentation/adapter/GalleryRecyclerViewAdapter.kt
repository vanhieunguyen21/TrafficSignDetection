package com.example.traffic_sign_detection.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.domain.data.model.GalleryImage
import com.example.traffic_sign_detection.domain.data.model.GalleryItem
import com.example.traffic_sign_detection.presentation.recyclerViewBinder.DataBindAdapter
import com.example.traffic_sign_detection.presentation.recyclerViewBinder.DataBinder
import com.example.traffic_sign_detection.presentation.recyclerViewBinder.GalleryImageViewBinder
import com.example.traffic_sign_detection.presentation.recyclerViewBinder.GalleryTitleViewBinder
import com.example.traffic_sign_detection.presentation.viewModel.GalleryViewModel

class GalleryRecyclerViewAdapter(
    val viewModel: GalleryViewModel
) : DataBindAdapter<GalleryItem>() {

    companion object {
        const val TITLE_VIEW_TYPE = 0
        const val IMAGE_VIEW_TYPE = 1
    }

    private val galleryItems: MutableList<GalleryItem> = mutableListOf()
    private val imageViewBinder = GalleryImageViewBinder(this)
    private val titleViewBinder = GalleryTitleViewBinder(this)

    fun addGalleryItems(items: List<GalleryItem>) {
        galleryItems.addAll(items)
        notifyItemRangeChanged(0, items.size)
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