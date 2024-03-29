package com.example.traffic_sign_detection.presentation.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traffic_sign_detection.domain.core.TFLiteClassifier
import com.example.traffic_sign_detection.domain.data.model.ClassificationResult
import com.example.traffic_sign_detection.domain.data.model.GalleryImage
import com.example.traffic_sign_detection.domain.enumeration.LoadDataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(private val classifier: TFLiteClassifier) : ViewModel() {
    private val _predictionState = MutableLiveData(LoadDataState.NONE)
    val predictionState: LiveData<LoadDataState> get() = _predictionState
    fun setPredictionState(state : LoadDataState){
        _predictionState.value = state
    }

    private var _result: ClassificationResult? = null
    val result: ClassificationResult? get() = _result

    var galleryImage : GalleryImage? = null
    var capturedImage : Bitmap? = null

    fun predict(bmp: Bitmap) {
        _predictionState.value = LoadDataState.LOADING
        viewModelScope.launch {
            try {
                _result = classifier.predict(bmp)
                _predictionState.value = LoadDataState.LOADED
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}