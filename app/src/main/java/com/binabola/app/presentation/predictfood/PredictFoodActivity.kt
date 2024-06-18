package com.binabola.app.presentation.predictfood

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.presentation.foodscan.FoodScanFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PredictFoodActivity : AppCompatActivity() {

    private lateinit var viewModel: PredictFoodViewModel
    private lateinit var previewView: PreviewView
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_food)

        previewView = findViewById(R.id.viewFinder)
        val captureButton: MaterialButton = findViewById(R.id.captureImage)
        val galleryButton: MaterialButton = findViewById(R.id.galleryButton)

        captureButton.setOnClickListener { dispatchTakePictureIntent() }
        galleryButton.setOnClickListener { openGallery() }

        viewModel = ViewModelProvider(this).get(PredictFoodViewModel::class.java)

        // Collect the prediction result
        lifecycleScope.launch {
            viewModel.predict.collect { result ->
                when (result) {
                    is Result.Success -> {
                        val foodName: String? = result.data.data?.foodPrediction
                        if (foodName != null) {
                            // Pass the food name to FoodScanFragment
                            val intent = Intent(this@PredictFoodActivity, FoodScanFragment::class.java).apply {
                                putExtra("FOOD_NAME", foodName)
                            }
                            startActivity(intent)
                        } else {
                            // Handle the case where foodName is null
                        }
                    }
                    is Result.Error -> {
                        // Handle the error
                    }
                    is Result.Loading -> {
                        // Show loading
                    }
                }
            }
        }

        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                // Convert bitmap to file and predict
                val imageFile = bitmapToFile(imageBitmap)
                viewModel.predictImage(imageFile)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri = result.data?.data
                val imageFile = uriToFile(selectedImageUri!!)
                viewModel.predictImage(imageFile)
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            takePictureLauncher.launch(takePictureIntent)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        // Convert bitmap to file
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
        return file
    }

    private fun uriToFile(uri: Uri): File {
        // Convert URI to file
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }
}
