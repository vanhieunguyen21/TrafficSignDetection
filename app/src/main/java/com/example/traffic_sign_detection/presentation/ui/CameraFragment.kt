package com.example.traffic_sign_detection.presentation.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.traffic_sign_detection.R
import com.example.traffic_sign_detection.databinding.FragmentCameraBinding
import com.example.traffic_sign_detection.presentation.base.BaseFragment
import com.example.traffic_sign_detection.presentation.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraFragment : BaseFragment<FragmentCameraBinding>() {
    companion object {
        private const val TAG = "CameraFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var outputDirectory: File
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun getLayoutRes(): Int = R.layout.fragment_camera

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up buttons
        binding.galleryButton.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_galleryFragment)
        }
        binding.closeButton.setOnClickListener { activity?.finish() }

        // Set up camera
        outputDirectory = getOutputDirectory()
        binding.captureButton.setOnClickListener { capture() }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onResume() {
        super.onResume()
        binding.viewFinder.post {
            startCamera()
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build()
                .also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()

            val viewPort = binding.viewFinder.viewPort ?: return@addListener
            val useCaseGroup = UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageCapture!!)
                .setViewPort(viewPort)
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)
            } catch (e: Exception) {
                Log.e(TAG, "Use case binding failed", e)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun capture() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has been taken
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("UnsafeOptInUsageError")
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    Log.d(TAG, "onCaptureSuccess")
                    val image = imageProxy.image ?: return

                    // Create bitmap image
                    var bmp = image.toBitmap()
                    image.close()
                    // Rotate image 90 degree (original image is horizontal)
                    val matrix = Matrix().apply { postRotate(90f) }
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
                    imageProxy.close()

                    // Save image
                    val fos = FileOutputStream(photoFile)
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush().also { fos.close() }

                    // Assign captured image to activity view model
                    activityViewModel.capturedImage = bmp
                    // Navigate to crop image fragment
                    val action = CameraFragmentDirections.actionCameraFragmentToCropImageFragment()
                    findNavController().navigate(action)
                }

                override fun onError(e: ImageCaptureException) {
                    Log.e(TAG, "Capture Error", e)
                }
            })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireActivity().externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

    private fun Image.toBitmap(): Bitmap {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}