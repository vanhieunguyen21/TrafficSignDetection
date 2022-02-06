package com.example.traffic_sign_detection.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.data.model.GalleryImage
import com.example.traffic_sign_detection.data.model.GalleryItem
import com.example.traffic_sign_detection.presentation.dataBinder.DataBindAdapter
import com.example.traffic_sign_detection.presentation.dataBinder.DataBinder
import com.example.traffic_sign_detection.presentation.dataBinder.GalleryImageViewBinder
import com.example.traffic_sign_detection.presentation.dataBinder.GalleryTitleViewBinder

class GalleryRecyclerViewAdapter :
    DataBindAdapter <GalleryItem>() {

    private val galleryItems : List<GalleryItem> = ArrayList()
    private val imageViewBinder = GalleryImageViewBinder(this)
    private val titleViewBinder = GalleryTitleViewBinder(this)

    override fun getItemCount(): Int {
        return galleryItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (galleryItems[position] is GalleryImage) 0 else 1
    }

    override fun getItem(position: Int): GalleryItem {
        return galleryItems[position]
    }

    override fun getDataBinder(viewType: Int): DataBinder<RecyclerView.ViewHolder, GalleryItem> {
        return if (viewType == 0) imageViewBinder else titleViewBinder
    }
}