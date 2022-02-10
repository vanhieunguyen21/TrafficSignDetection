package com.example.traffic_sign_detection.bindingAdapter

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter(value = ["imageUri"], requireAll = false)
fun setImageUri(view: ImageView, uri: Uri) {
    view.setImageURI(uri)
}

@BindingAdapter("imageResource")
fun setImageResource(view: ImageView, resourceId: Int) {
    view.setImageResource(resourceId)
}


@BindingAdapter("imageBitmap")
fun setImageBitmap(view: ImageView, bmp: Bitmap) {
    view.setImageBitmap(bmp)
}
