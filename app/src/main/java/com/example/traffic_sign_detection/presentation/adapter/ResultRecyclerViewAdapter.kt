package com.example.traffic_sign_detection.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.domain.data.model.Prediction
import com.example.traffic_sign_detection.databinding.ItemSignResultViewHolderBinding
import com.example.traffic_sign_detection.util.ContextUtil
import com.squareup.picasso.Picasso

class ResultRecyclerViewAdapter(
    private val predictions: List<Prediction>
) : RecyclerView.Adapter<ResultRecyclerViewAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemBinding =
            ItemSignResultViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ImageViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val prediction = predictions[position]
        holder.bind(prediction)
    }

    override fun getItemCount(): Int {
        return predictions.size
    }

    class ImageViewHolder(private val itemBinding: ItemSignResultViewHolderBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(prediction: Prediction) {
            // Set accuracy
            itemBinding.accuracy.text = String.format("%.2f%%", prediction.accuracy * 100)

            // Set image
            val imageResourceId =
                ContextUtil.getDrawableResourceId(itemBinding.root.context, prediction.drawable)
            Picasso.get()
                .load(imageResourceId)
                .resize(200, 200)
                .into(itemBinding.imageView)
        }
    }
}