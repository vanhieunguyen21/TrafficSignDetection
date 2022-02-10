package com.example.traffic_sign_detection.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.traffic_sign_detection.domain.data.model.SignMetadata
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SignDetailViewModel @AssistedInject constructor(
    @Assisted val signMetadata: SignMetadata
) : ViewModel() {

    @AssistedFactory
    interface SignDetailViewModelFactory {
        fun createSignDetailViewModel(signMetadata: SignMetadata): SignDetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: SignDetailViewModelFactory,
            signMetadata: SignMetadata
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.createSignDetailViewModel(signMetadata) as T
            }
        }
    }
}