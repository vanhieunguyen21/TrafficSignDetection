package com.example.traffic_sign_detection

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.traffic_sign_detection.presentation.ui.camera.CameraFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_CODE_PERMISSION = 1
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        addFragment(CameraFragment(), "CameraFragment", false)
//        val classifier = TFLiteClassifier(this)
//        val image1 = BitmapFactory.decodeResource(resources, R.drawable.image0)
//        classifier.predict(image1)
//        val image2 = BitmapFactory.decodeResource(resources, R.drawable.image1)
//        classifier.predict(image2)

        // Request all permission
        if (allPermissionGranted()) {
            // Do nothing
        } else {
            requestRequiredPermissions()
        }
    }

    fun requestRequiredPermissions(){
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
        Log.d(TAG, "addFragment: $tag")
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.container, frag, tag)
        if (addToBackstack) transaction.addToBackStack(null)
        transaction.commit()
    }
}