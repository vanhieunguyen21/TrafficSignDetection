package com.example.traffic_sign_detection.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.traffic_sign_detection.R
import com.example.traffic_sign_detection.databinding.FragmentGalleryBinding
import com.example.traffic_sign_detection.presentation.adapter.GalleryRecyclerViewAdapter
import com.example.traffic_sign_detection.presentation.base.BaseFragment
import com.example.traffic_sign_detection.presentation.viewModel.GalleryViewModel
import com.example.traffic_sign_detection.presentation.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : BaseFragment<FragmentGalleryBinding>() {

    companion object {
        private const val TAG = "GalleryFragment"
    }

    override fun getLayoutRes(): Int = R.layout.fragment_gallery
    private val viewModel: GalleryViewModel by viewModels()
    private val activityViewModel : MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up buttons
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        // Set up recycler view
        val adapter = viewModel.adapter
        val spanCount = 4
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == GalleryRecyclerViewAdapter.TITLE_VIEW_TYPE) spanCount
                else 1
            }
        }
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        setFragmentResultListener("croppedImage") { _, bundle ->
            setFragmentResult("selectedImage", bundle)
            findNavController().popBackStack()
        }

        // Observe if user select an image from recycler view
        viewModel.selectedImage.observe(viewLifecycleOwner) { image ->
            if (image != null) {
                // Assign selected image to activity view model
                activityViewModel.galleryImage = image
                // Remove value to avoid re-trigger when fragment is recreated
                viewModel.setSelectedImage(null)
                // Navigate to crop image fragment
                val action = GalleryFragmentDirections
                    .actionGalleryFragmentToCropImageFragment()
                findNavController().navigate(action)
            }
        }
    }

}