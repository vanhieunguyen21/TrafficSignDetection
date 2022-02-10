package com.example.traffic_sign_detection.presentation.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.traffic_sign_detection.presentation.adapter.ResultRecyclerViewAdapter
import com.example.traffic_sign_detection.domain.data.model.ClassificationResult
import com.example.traffic_sign_detection.domain.data.model.SignMetadata
import com.example.traffic_sign_detection.util.ClassificationResultUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ResultViewModel @AssistedInject constructor(
    @Assisted private val result: ClassificationResult
) : ViewModel() {

    val adapter = ResultRecyclerViewAdapter(
        ClassificationResultUtil.filterHighAccuracyPrediction(result.predictions),
        this
    )
    private val _selectedSign = MutableLiveData<SignMetadata?>(null)
    val selectedSign: LiveData<SignMetadata?> get() = _selectedSign
    fun setSelectedSign(sign: SignMetadata?) {
        _selectedSign.value = sign
    }

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