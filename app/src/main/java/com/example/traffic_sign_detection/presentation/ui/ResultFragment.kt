package com.example.traffic_sign_detection.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.traffic_sign_detection.R
import com.example.traffic_sign_detection.databinding.FragmentResultBinding
import com.example.traffic_sign_detection.presentation.base.BaseFragment
import com.example.traffic_sign_detection.presentation.viewModel.MainViewModel
import com.example.traffic_sign_detection.presentation.viewModel.ResultViewModel
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultFragment : BaseFragment<FragmentResultBinding>() {
    companion object {
        private const val TAG = "ResultFragment"
    }

    override fun getLayoutRes(): Int = R.layout.fragment_result
    private lateinit var viewModel: ResultViewModel
    private val activityViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var resultViewModelFactory: ResultViewModel.ResultViewModelFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        val result = activityViewModel.result
        if (result == null) {
            Log.e(TAG, "onViewCreated: result is null")
            findNavController().popBackStack()
            return
        }

        // Instantiate ViewModel
        val tmpViewModel: ResultViewModel by viewModels {
            ResultViewModel.provideFactory(resultViewModelFactory, result)
        }
        viewModel = tmpViewModel

        // Set button onclick
        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        // Bind data
        binding.result = viewModel.result

        // Set up RecyclerView
        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.apply {
            justifyContent = JustifyContent.CENTER
            alignItems = AlignItems.CENTER
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        binding.resultRecyclerView.layoutManager = layoutManager
        binding.resultRecyclerView.adapter = viewModel.adapter

        // Listen to user selection on sign
        viewModel.selectedSign.observe(viewLifecycleOwner) { signMetadata ->
            if (signMetadata == null) {
                return@observe
            }
            // Remove value to prevent recall on fragment recreation
            viewModel.setSelectedSign(null)
            // Initiate navigation action with argument
            val action = ResultFragmentDirections.actionResultFragmentToSignDetailFragment(signMetadata)
            // Navigate to detail fragment
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}