package com.example.traffic_sign_detection.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.traffic_sign_detection.R
import com.example.traffic_sign_detection.databinding.FragmentCropImageBinding
import com.example.traffic_sign_detection.presentation.base.BaseFragment
import com.example.traffic_sign_detection.presentation.viewModel.MainViewModel
import kotlinx.coroutines.launch

class CropImageFragment : BaseFragment<FragmentCropImageBinding>() {

    companion object {
        private const val TAG = "CropImageFragment"
    }

    override fun getLayoutRes(): Int = R.layout.fragment_crop_image
    private val activityViewModel : MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        // Set up buttons
        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        // Get image uri
        val args: Bundle? = arguments
        if (args == null) {
            Log.e(TAG, "arguments null")
            findNavController().popBackStack()
            return
        }
        val imageSource : String? = args.getString("image")
        if (imageSource == null){
            Log.e(TAG, "image null")
            findNavController().popBackStack()
            return
        }

        lifecycleScope.launch { initCrop(imageSource) }
    }

    private fun initCrop(imageSource: String) {
        // Set crop rect to square
        binding.cropView.setAspectRatio(1, 1)

        // Set image source to crop
        if (imageSource == "camera") {
            if (activityViewModel.capturedImage == null) {
                Log.e(TAG, "initCrop: image source from camera is null")
                return
            }
            binding.cropView.setImageBitmap(activityViewModel.capturedImage)
        } else if (imageSource == "gallery") {
            if (activityViewModel.galleryImage == null) {
                Log.e(TAG, "initCrop: image source from gallery is null")
                return
            }
            binding.cropView.setImageUriAsync(activityViewModel.galleryImage!!.uri)
        }

        // Set crop action
        binding.finishButton.setOnClickListener {
            val croppedImage = binding.cropView.croppedImage
            findNavController().popBackStack()
            croppedImage?.let { activityViewModel.predict(croppedImage) }
        }
    }
}