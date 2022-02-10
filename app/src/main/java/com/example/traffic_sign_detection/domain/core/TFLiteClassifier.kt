package com.example.traffic_sign_detection.domain.core

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.traffic_sign_detection.domain.application.GlobalData
import com.example.traffic_sign_detection.domain.data.model.ClassificationResult
import com.example.traffic_sign_detection.domain.data.model.Prediction
import com.example.traffic_sign_detection.ml.Cp10Loss005h5
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class TFLiteClassifier(private val context: Context) {

    companion object {
        private const val TAG = "TFLiteClassifier"
    }

    // Load model
    private val model: Cp10Loss005h5 = Cp10Loss005h5.newInstance(context)
    private val inputShape = intArrayOf(64, 64, 3)

    suspend fun predict(bmp: Bitmap): ClassificationResult {
        // Preprocess image
        val imageData = preprocess(bmp)

        // Create input buffer with image
        val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 64, 64, 3), DataType.FLOAT32)
        inputFeature.loadBuffer(imageData)

        // Run model and get result
        val output = model.process(inputFeature)
        val outputFeature0 = output.outputFeature0AsTensorBuffer
        val floatBuffer = (outputFeature0.buffer.rewind() as ByteBuffer).asFloatBuffer()
        // Array to hold prediction result
        val floatArray = FloatArray(floatBuffer.limit())
        floatBuffer.rewind()
        // Put result in array
        floatBuffer.get(floatArray)
        // Instantiate prediction result objects
        val predictions: MutableList<Prediction> = ArrayList()
        for ((idx, value) in floatArray.withIndex()) {
            val metadata = GlobalData.metaDataMap[idx] ?: continue
            predictions.add(
                Prediction(
                    value,
                    metadata.id,
                    metadata.label,
                    metadata.name,
                    metadata.drawable,
                    metadata.description,
                    metadata.drawableResourceId
                )
            )
        }

        val sortedPredictions = predictions.sortedByDescending { it.accuracy }
        // Instantiate classification result object
        val result = ClassificationResult(bmp, sortedPredictions)
        Log.d(TAG, "predict: $sortedPredictions")
        return result
    }

    /**
     * Function to preprocess input image into a ByteBuffer of image
     * with shape as the same as input shape of the model
     * and normalize values of image to range of 0 and 1
     * @param: bmp: input bitmap
     * @return preprocessed byte buffer
     **/
    private fun preprocess(bmp: Bitmap): ByteBuffer {
        val imageData =
            ByteBuffer.allocateDirect(Float.SIZE_BYTES * inputShape[0] * inputShape[1] * inputShape[2])
        imageData.order(ByteOrder.nativeOrder())
        // Scale image to match input shape of model
        val bitmap = Bitmap.createScaledBitmap(bmp, inputShape[0], inputShape[1], true)
        // Get pixels of the image and put it in an array
        val intValues = IntArray(inputShape[0] * inputShape[1])
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        // Normalize pixels to range 0 and 1 and put them to byte buffer
        var pixelIndex = 0
        for (i in 0 until inputShape[0]) {
            for (j in 0 until inputShape[1]) {
                val value = intValues[pixelIndex++]
                // Put normalized pixel of 3 channels from bitmap to byte buffer
                imageData.putFloat(((value shr 16) and 0xFF) / 255f)
                imageData.putFloat(((value shr 8) and 0xFF) / 255f)
                imageData.putFloat((value and 0xFF) / 255f)
            }
        }
        return imageData
    }
}