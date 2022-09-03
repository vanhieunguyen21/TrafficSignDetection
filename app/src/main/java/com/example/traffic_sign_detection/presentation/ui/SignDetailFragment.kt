package com.example.traffic_sign_detection.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.traffic_sign_detection.R
import com.example.traffic_sign_detection.databinding.FragmentSignDetailBinding
import com.example.traffic_sign_detection.domain.data.model.SignMetadata
import com.example.traffic_sign_detection.presentation.base.BaseFragment
import com.example.traffic_sign_detection.presentation.viewModel.SignDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignDetailFragment : BaseFragment<FragmentSignDetailBinding>() {

    companion object {
        private const val TAG = "SignDetailFragment"
    }

    override fun getLayoutRes(): Int = R.layout.fragment_sign_detail
    private lateinit var viewModel: SignDetailViewModel

    @Inject
    lateinit var signDetailViewModelFactory: SignDetailViewModel.SignDetailViewModelFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        // Get sign metadata from arguments
        val args: Bundle? = arguments
        if (args == null) {
            Log.e(TAG, "arguments null")
            findNavController().popBackStack()
            return
        }
        val signMetadata: SignMetadata? =
            args.getSerializable("signMetadata") as SignMetadata?
        if (signMetadata == null) {
            Log.e(TAG, "result null")
            findNavController().popBackStack()
            return
        }

        // Instantiate ViewModel
        val tmpViewModel: SignDetailViewModel by viewModels {
            SignDetailViewModel.provideFactory(signDetailViewModelFactory, signMetadata)
        }
        viewModel = tmpViewModel

        // Set button onclick
        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        // Bind data
        binding.signMetadata = viewModel.signMetadata
    }
}