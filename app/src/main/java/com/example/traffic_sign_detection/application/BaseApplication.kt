package com.example.traffic_sign_detection.application

import android.app.Application
import android.util.Log
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.traffic_sign_detection.data.repository.MediaStoreImageRepository
import com.example.traffic_sign_detection.data.repository.SignMetadataRepository
import com.example.traffic_sign_detection.enumeration.LoadDataState
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), CameraXConfig.Provider {
    companion object {
        private const val TAG = "BaseApplication"
    }

    @Inject
    lateinit var signSignMetadataRepository: SignMetadataRepository
    @Inject
    lateinit var mediaStoreImageRepository: MediaStoreImageRepository
    private val _loadMetaDataState = MutableLiveData(LoadDataState.NONE)
    val loadMetaDataState: LiveData<LoadDataState> get() = _loadMetaDataState

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()

        // Create custom coroutine scope
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        // Load all sign meta data and map it to global data
        applicationScope.launch {
            _loadMetaDataState.value = LoadDataState.LOADING
            try {
                val metaDataList = signSignMetadataRepository.getAllSignMetadata()
                metaDataList.iterator().forEach {
                    GlobalData.metaDataMap[it.id] = it
                }
                Log.d(TAG, "Load meta data completed")
                _loadMetaDataState.value = LoadDataState.LOADED
            } catch (e: Exception) {
                Log.e(TAG, "Error loading sign meta data", e)
                _loadMetaDataState.value = LoadDataState.ERROR
            }
        }
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}