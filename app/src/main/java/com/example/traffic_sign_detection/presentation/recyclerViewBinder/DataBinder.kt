package com.example.traffic_sign_detection.presentation.recyclerViewBinder

import android.view.ViewGroup
import com.example.traffic_sign_detection.domain.data.model.GalleryItem

abstract class DataBinder<T, I : GalleryItem>(
    val mDataBindAdapter: DataBindAdapter<I>
) {
    abstract fun newViewHolder(parent: ViewGroup): T
    abstract fun bindViewHolder(holder: T, position: Int)
}