package com.example.traffic_sign_detection.presentation.ui.result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traffic_sign_detection.data.model.ClassificationResult
import com.example.traffic_sign_detection.databinding.FragmentResultBinding
import com.google.android.flexbox.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultFragment : Fragment() {
    companion object {
        private const val TAG = "ResultFragment"
    }

    private lateinit var binding: FragmentResultBinding
    private lateinit var viewModel: ResultViewModel
    @Inject
    lateinit var resultViewModelFactory: ResultViewModel.ResultViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        // Get results from arguments
        val args: Bundle? = arguments
        if (args == null) {
            Log.e(TAG, "arguments null")
            parentFragmentManager.popBackStack()
            return
        }
        val result: ClassificationResult? =
            args.getSerializable("result") as ClassificationResult?
        if (result == null) {
            Log.e(TAG, "result null")
            parentFragmentManager.popBackStack()
            return
        }

        // Instantiate ViewModel
        val tmpViewModel: ResultViewModel by viewModels {
            ResultViewModel.provideFactory(resultViewModelFactory, result)
        }
        viewModel = tmpViewModel

        // Set image
        binding.imageView.setImageBitmap(result.bmp)

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
    }
}