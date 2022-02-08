package com.example.traffic_sign_detection.presentation.dataBinder

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.data.model.GalleryImage
import com.example.traffic_sign_detection.data.model.GalleryItem
import com.example.traffic_sign_detection.databinding.ItemGalleryImageBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryImageViewBinder(
    mDataBindAdapter: DataBindAdapter<GalleryItem>
) : DataBinder<RecyclerView.ViewHolder, GalleryItem>(
    mDataBindAdapter
) {

    companion object {
        private const val TAG = "GalleryImageViewBinder"
    }

    override fun newViewHolder(parent: ViewGroup): GalleryImageViewHolder {
        val itemBinding = ItemGalleryImageBinding.inflate(LayoutInflater.from(parent.context))
        return GalleryImageViewHolder(itemBinding)
    }

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val image = mDataBindAdapter.getItem(position) as GalleryImage
        val mHolder = holder as GalleryImageViewHolder
        mHolder.bind(image)
    }

    class GalleryImageViewHolder(private val itemBinding: ItemGalleryImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(image: GalleryImage) {
            val context = itemView.context
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val thumbnail: Bitmap =
                            context.contentResolver.loadThumbnail(
                                image.uri,
                                Size(400, 400),
                                null
                            )
                        launch(Dispatchers.Main) {
                            itemBinding.imageView.setImageBitmap(thumbnail)
                        }
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "bind GalleryImageViewHolder: " +
                                    "failed loading thumbnail ${image.bucketName}/${image.name}",
                            e
                        )
                    }
                }
            } else {
                Picasso.get()
                    .load(image.uri)
                    .centerCrop().fit()
                    .resize(400, 400)
                    .into(itemBinding.imageView)
            }
        }
    }
}