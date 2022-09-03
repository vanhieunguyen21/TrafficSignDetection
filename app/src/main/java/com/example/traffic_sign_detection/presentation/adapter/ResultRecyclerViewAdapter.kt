package com.example.traffic_sign_detection.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traffic_sign_detection.domain.data.model.Prediction
import com.example.traffic_sign_detection.databinding.ItemSignResultViewHolderBinding
import com.example.traffic_sign_detection.presentation.viewModel.ResultViewModel

class ResultRecyclerViewAdapter(
    private val predictions: List<Prediction>,
    val viewModel: ResultViewModel
) : RecyclerView.Adapter<ResultRecyclerViewAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemBinding =
            ItemSignResultViewHolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ImageViewHolder(itemBinding, this)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val prediction = predictions[position]
        holder.bind(prediction)
    }

    override fun getItemCount(): Int {
        return predictions.size
    }

    class ImageViewHolder(
        private val itemBinding: ItemSignResultViewHolderBinding,
        private val adapter : ResultRecyclerViewAdapter
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(prediction: Prediction) {
            // Bind data
            itemBinding.prediction = prediction

            // Set on click
            itemBinding.root.setOnClickListener {
                adapter.viewModel.setSelectedSign(prediction)
            }
        }
    }
}