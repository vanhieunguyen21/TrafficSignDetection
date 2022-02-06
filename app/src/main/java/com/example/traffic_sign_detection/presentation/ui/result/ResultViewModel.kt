package com.example.traffic_sign_detection.presentation.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.traffic_sign_detection.presentation.adapter.ResultRecyclerViewAdapter
import com.example.traffic_sign_detection.data.model.ClassificationResult
import com.example.traffic_sign_detection.util.ClassificationResultUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ResultViewModel @AssistedInject constructor(
    @Assisted private val result: ClassificationResult
) : ViewModel() {

    val adapter = ResultRecyclerViewAdapter(
        ClassificationResultUtil.filterHighAccuracyPrediction(result.predictions)
    )

    @AssistedFactory
    interface ResultViewModelFactory {
        fun createResultViewModel(result: ClassificationResult): ResultViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: ResultViewModelFactory,
            result: ClassificationResult
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("unchecked_cast")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.createResultViewModel(result) as T
            }
        }
    }
}