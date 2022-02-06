package com.example.traffic_sign_detection.presentation.dataBinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.data.model.GalleryImage
import com.example.traffic_sign_detection.data.model.GalleryItem
import com.example.traffic_sign_detection.databinding.ItemGalleryImageBinding

class GalleryImageViewBinder(mDataBindAdapter: DataBindAdapter<GalleryItem>) :
    DataBinder<RecyclerView.ViewHolder, GalleryItem>(
        mDataBindAdapter
    ) {

    override fun newViewHolder(parent: ViewGroup): GalleryImageViewHolder {
        val itemBinding = ItemGalleryImageBinding.inflate(LayoutInflater.from(parent.context))
        return GalleryImageViewHolder(itemBinding)
    }

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val image = mDataBindAdapter.getItem(position) as GalleryImage
        val mHolder = holder as GalleryImageViewHolder
        mHolder.bind(image)
    }

    class GalleryImageViewHolder(itemBinding: ItemGalleryImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(image: GalleryImage) {
            // TODO: bind data
        }
    }
}