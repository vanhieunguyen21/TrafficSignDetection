package com.example.traffic_sign_detection.presentation.dataBinder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.domain.data.model.GalleryItem

abstract class DataBindAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getDataBinder(viewType).newViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getDataBinder(holder.itemViewType).bindViewHolder(holder, position)
    }

    abstract override fun getItemCount(): Int
    abstract override fun getItemViewType(position: Int): Int
    abstract fun getItem(position: Int): T
    abstract fun getDataBinder(viewType: Int): DataBinder<RecyclerView.ViewHolder, GalleryItem>

}