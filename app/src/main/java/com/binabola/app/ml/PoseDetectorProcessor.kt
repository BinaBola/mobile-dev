package com.binabola.app.ml

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions

class PoseDetectorProcessor {
    private val detector: PoseDetector

    private val executor = TaskExecutors.MAIN_THREAD

    init {
        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
            .build()
        detector = PoseDetection.getClient(options)
    }

    @OptIn(ExperimentalGetImage::class) @SuppressLint("UnsafeExperimentalUsageError")
    fun processImageProxy(image: ImageProxy, onDetectionFinished: (Pose) -> Unit) {
        detector.process(InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees))
            .addOnSuccessListener(executor) { results: Pose -> onDetectionFinished(results) }
            .addOnFailureListener(executor) { e: Exception ->
                Log.e("Camera", "Error detecting pose", e)
            }
            .addOnCompleteListener { image.close() }
    }
}