package com.example.traffic_sign_detection.presentation.dataBinder

import android.view.ViewGroup
import com.example.traffic_sign_detection.data.model.GalleryItem

abstract class DataBinder<T, I : GalleryItem>(
    protected val mDataBindAdapter: DataBindAdapter<I>
) {
    abstract fun newViewHolder(parent: ViewGroup): T
    abstract fun bindViewHolder(holder: T, position: Int)
}