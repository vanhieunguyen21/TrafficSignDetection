package com.example.traffic_sign_detection.presentation.ui.gallery

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.traffic_sign_detection.databinding.FragmentGalleryBinding
import com.example.traffic_sign_detection.presentation.MainActivity
import com.example.traffic_sign_detection.presentation.adapter.GalleryRecyclerViewAdapter
import com.example.traffic_sign_detection.presentation.ui.cropImage.CropImageFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private val viewModel: GalleryViewModel by viewModels()

    companion object {
        private const val TAG = "GalleryFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = FragmentGalleryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up buttons
        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }

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

        // Observe if user select an image from recycler view
        viewModel.selectedImage.observe(viewLifecycleOwner) { image ->
            if (image != null) {
                val mainActivity: MainActivity = requireActivity() as MainActivity
                val cropFragment = CropImageFragment()
                val args = Bundle()
                args.putSerializable("image", image)
                cropFragment.arguments = args

                mainActivity.addFragment(cropFragment, "CropFragment", true)

                setFragmentResultListener("croppedImage") { _, bundle ->
                    Log.d(TAG, "FragmentResultListener: $bundle")
                    parentFragmentManager.popBackStack()
                    setFragmentResult("selectedImage", bundle)
                }
                // Remove value to avoid re-trigger when fragment is recreated
                viewModel.setSelectedImage(null)
            }
        }
    }

}