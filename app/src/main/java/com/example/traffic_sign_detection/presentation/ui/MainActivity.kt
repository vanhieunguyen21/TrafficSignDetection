package com.example.traffic_sign_detection.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.traffic_sign_detection.R
import com.example.traffic_sign_detection.databinding.ActivityMainBinding
import com.example.traffic_sign_detection.domain.enumeration.LoadDataState
import com.example.traffic_sign_detection.presentation.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()

    companion object {
        private const val TAG = "MainActivity"
        private const val DEBUG = true
        private const val REQUEST_CODE_PERMISSION = 1
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (DEBUG) Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        // Set window to edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Inflate layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle overlapping navigation bar (bottom) by adding margin
        ViewCompat.setOnApplyWindowInsetsListener(binding.container) {view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }

        // Request all permission
        if (allPermissionGranted()) {
            // Do nothing
        } else {
            requestRequiredPermissions()
        }

        // Observe prediction result
        viewModel.predictionState.observe(this) { state ->
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
                    Log.d(TAG, "onCreate: show result")
                    // TODO: hide loading
                    // Set value to NONE to avoid reload on configuration change
                    viewModel.setPredictionState(LoadDataState.NONE)
                    // Open result fragment
                    findNavController(R.id.container).navigateUp()
                    findNavController(R.id.container).navigate(R.id.resultFragment)
                }
                else -> {
                    // Do nothing
                }
            }
        }
    }

    private fun requestRequiredPermissions(){
        requestPermissions(
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSION
        )
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (allPermissionGranted()) {
                // Do nothing
            } else {
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addFragment(frag: Fragment, tag: String?, addToBackstack: Boolean) {
        if (DEBUG) Log.d(TAG, "addFragment: $tag")
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.container, frag, tag)
        if (addToBackstack) transaction.addToBackStack(null)
        transaction.commit()
    }
}