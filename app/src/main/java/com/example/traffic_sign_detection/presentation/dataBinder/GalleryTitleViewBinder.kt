package com.example.traffic_sign_detection.presentation.dataBinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.domain.data.model.GalleryItem
import com.example.traffic_sign_detection.domain.data.model.GalleryTitle
import com.example.traffic_sign_detection.databinding.ItemGalleryTitleBinding

class GalleryTitleViewBinder(mDataBindAdapter: DataBindAdapter<GalleryItem>) :
    DataBinder<RecyclerView.ViewHolder, GalleryItem>(
        mDataBindAdapter
    ) {

    class GalleryTitleViewHolder(private val itemBinding: ItemGalleryTitleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(title: GalleryTitle) {
            itemBinding.title.text = title.bucketName
        }
    }

    override fun newViewHolder(parent: ViewGroup): GalleryTitleViewHolder {
        val itemBinding = ItemGalleryTitleBinding.inflate(LayoutInflater.from(parent.context))
        return GalleryTitleViewHolder(itemBinding)
    }

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val title = mDataBindAdapter.getItem(position) as GalleryTitle
        val mHolder = holder as GalleryTitleViewHolder
        mHolder.bind(title)

    }
}