package com.example.traffic_sign_detection.presentation.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.traffic_sign_detection.databinding.FragmentSignDetailBinding
import com.example.traffic_sign_detection.domain.data.model.SignMetadata
import com.example.traffic_sign_detection.util.ContextUtil
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignDetailFragment : Fragment() {

    companion object {
        private const val TAG = "SignDetailFragment"
    }

    private lateinit var binding: FragmentSignDetailBinding
    private lateinit var viewModel: SignDetailViewModel

    @Inject
    lateinit var signDetailViewModelFactory: SignDetailViewModel.SignDetailViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = FragmentSignDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        // Get sign metadata from arguments
        val args: Bundle? = arguments
        if (args == null) {
            Log.e(TAG, "arguments null")
            parentFragmentManager.popBackStack()
            return
        }
        val signMetadata: SignMetadata? =
            args.getSerializable("signMetadata") as SignMetadata?
        if (signMetadata == null) {
            Log.e(TAG, "result null")
            parentFragmentManager.popBackStack()
            return
        }

        // Instantiate ViewModel
        val tmpViewModel: SignDetailViewModel by viewModels {
            SignDetailViewModel.provideFactory(signDetailViewModelFactory, signMetadata)
        }
        viewModel = tmpViewModel

        // Set button onclick
        binding.backButton.setOnClickListener { parentFragmentManager.popBackStack() }

        // Bind data
        binding.signMetadata = viewModel.signMetadata
    }
}