package com.example.traffic_sign_detection.presentation.ui.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.traffic_sign_detection.presentation.MainActivity
import com.example.traffic_sign_detection.R
import com.example.traffic_sign_detection.databinding.FragmentCameraBinding
import com.example.traffic_sign_detection.domain.enumeration.LoadDataState
import com.example.traffic_sign_detection.presentation.ui.gallery.GalleryFragment
import com.example.traffic_sign_detection.presentation.ui.result.ResultFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraFragment : Fragment() {
    companion object {
        private const val TAG = "CameraFragment"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var outputDirectory: File
    private val viewModel: CameraViewModel by viewModels()
    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = FragmentCameraBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up buttons
        binding.galleryButton.setOnClickListener {
            openGalleryFragment()
            setFragmentResultListener("selectedImage") { _, bundle ->
                Log.d(TAG, "FragmentResultListener $bundle")
                val bmp = bundle.getParcelable("croppedImage") as Bitmap?
                if (bmp != null) {
                    viewModel.predict(bmp)
                }
            }
        }
        binding.closeButton.setOnClickListener { requireActivity().finish() }

        // Set up camera
        outputDirectory = getOutputDirectory()
        binding.captureButton.setOnClickListener { capture() }
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.viewFinder.post {
            startCamera()
        }

        // Observe prediction result
        viewModel.predictionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                LoadDataState.NONE -> {
                    // Do nothing
                }
                LoadDataState.LOADING -> {
                    // TODO: show loading
                }
                LoadDataState.ERROR -> {
                    // TODO: show error
                }
                LoadDataState.LOADED -> {
                    // TODO: hide loading
                    // Open result fragment
                    val activity: MainActivity = activity as MainActivity
                    val resultFragment = ResultFragment()
                    val args = Bundle()
                    args.putSerializable("result", viewModel.result)
                    resultFragment.arguments = args
                    activity.addFragment(resultFragment, "ResultFragment", true)
                    // Set value to NONE to avoid reload on configuration change
                    viewModel.setPredictionState(LoadDataState.NONE)
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

    private fun openGalleryFragment() {
        val activity: MainActivity = requireActivity() as MainActivity
        activity.addFragment(GalleryFragment(), "GalleryFragment", true)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val viewPort = binding.viewFinder.viewPort
            val useCaseGroup = UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageCapture!!)
                .setViewPort(viewPort!!)
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
                    // Crop and rotate image to focus rectangle
                    val imageWidth = imageProxy.cropRect.right - imageProxy.cropRect.left
                    val imageHeight = imageProxy.cropRect.bottom - imageProxy.cropRect.top
                    val cropX =
                        (binding.focusRect.y * imageHeight / binding.viewFinder.width).toInt()
                    val cropY =
                        (binding.focusRect.x * imageWidth / binding.viewFinder.height).toInt() + imageProxy.cropRect.top
                    val cropWidth =
                        (binding.focusRect.width.toFloat() * imageHeight / binding.viewFinder.width).toInt()
                    val cropHeight =
                        (binding.focusRect.height.toFloat() * imageWidth / binding.viewFinder.height).toInt()

                    // Create bitmap image from cropped region
                    var bmp = image.toBitmap(cropX, cropY, cropWidth, cropHeight)
                    image.close()
                    // Rotate image 90 degree (original image is horizontal)
                    val matrix = Matrix().apply { postRotate(90f) }
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
                    imageProxy.close()

                    // Save image
                    val fos = FileOutputStream(photoFile)
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.flush().also { fos.close() }

                    // Start predicting
                    viewModel.predict(bmp)
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

    private fun Image.toBitmap(x: Int, y: Int, width: Int, height: Int): Bitmap {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        return Bitmap.createBitmap(bmp, x, y, width, height)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}