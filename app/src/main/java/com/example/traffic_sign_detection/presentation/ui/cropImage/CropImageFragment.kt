package com.example.traffic_sign_detection.presentation.ui.cropImage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.traffic_sign_detection.domain.data.model.GalleryImage
import com.example.traffic_sign_detection.databinding.FragmentCropImageBinding

class CropImageFragment : Fragment() {

    companion object {
        private const val TAG = "CropImageFragment"
    }
    private lateinit var binding : FragmentCropImageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = FragmentCropImageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        // Get image uri
        val args: Bundle? = arguments
        if (args == null) {
            Log.e(TAG, "arguments null")
            parentFragmentManager.popBackStack()
            return
        }
        val galleryImage : GalleryImage? = args.getSerializable("image") as GalleryImage?
        if (galleryImage == null){
            Log.e(TAG, "image null")
            parentFragmentManager.popBackStack()
            return
        }

        // Set crop rect to square
        binding.cropView.setAspectRatio(1, 1)
        // Set image to crop
        binding.cropView.setImageUriAsync(galleryImage.uri)
        // Set crop action
        binding.finishButton.setOnClickListener {
            val croppedImage = binding.cropView.croppedImage
            val resultBundle = Bundle()
            resultBundle.putParcelable("croppedImage", croppedImage)
            parentFragmentManager.popBackStack()
            setFragmentResult("croppedImage", resultBundle)
        }
    }
}