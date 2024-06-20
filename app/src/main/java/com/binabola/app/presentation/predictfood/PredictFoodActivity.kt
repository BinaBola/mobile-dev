package com.binabola.app.presentation.predictfood

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.databinding.ActivityPredictFoodBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.foodscan.FoodScanActivity
import com.binabola.app.utils.getImageUri
import com.binabola.app.utils.reduceFileImage
import com.binabola.app.utils.uriToFile
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class PredictFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPredictFoodBinding
    private val viewModel by viewModels<PredictFoodViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var previewView: ImageView
    private var imageUri: Uri? = null


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            showImage()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[CAMERA_PERMISSION] ?: false -> {
                    Toast.makeText(this, "Camera permission request granted", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPredictFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previewView = findViewById(R.id.viewFinder)
        val captureButton: MaterialButton = findViewById(R.id.captureImage)
        val galleryButton: MaterialButton = findViewById(R.id.galleryButton)

        captureButton.setOnClickListener {
            if(!checkPermission(CAMERA_PERMISSION)) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        CAMERA_PERMISSION,
                    )
                )
                return@setOnClickListener
            }

            startCamera()
        }
        galleryButton.setOnClickListener {
            startGallery()
        }

        // Collect the prediction result
        lifecycleScope.launch {
            viewModel.predict.collect { result ->
                Log.d(TAG,"collect triggered! $result")
                when (result) {
                    is Result.Success -> {
                        val foodName: String? = result.data.foodPrediction
                        val foodCalorie: String? = result.data.calories.toString()
                        if (foodName != null) {
                            // Pass the food name back to FoodScanFragment
                            val resultIntent = Intent()
                            resultIntent.putExtra(FoodScanActivity.EXTRA_FOOD_VALUE, foodName)
                            resultIntent.putExtra(FoodScanActivity.EXTRA_CALORIE_VALUE, foodCalorie)
                            resultIntent.putExtra(FoodScanActivity.EXTRA_IMG_URI, imageUri.toString())
                            setResult(FoodScanActivity.RESULT_CODE_SCAN, resultIntent)
                            finish()
                        } else {
                            // Handle the case where foodName is null
                            AppUtil().showToast(this@PredictFoodActivity, "Makanan tidak dikenali")
                        }
                    }
                    is Result.Error -> {
                        AppUtil().showToast(this@PredictFoodActivity, result.error)

                    }
                    is Result.Loading -> {
                        // Show loading
                    }
                }
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        imageUri = getImageUri(this)
        launcherIntentCamera.launch(imageUri!!)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun showImage() {
        imageUri?.let {
            val options = RequestOptions().transform(RoundedCorners(48))
            Glide.with(binding.root.context).load(it).apply(options).into(binding.viewFinder)
            uploadImage()
        }
    }

    private fun uploadImage() {
        imageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )
            Log.d(TAG, multipartBody.toString())

            lifecycleScope.launch {
                viewModel.predictImage(multipartBody)
            }
        }
    }


    companion object{
        private const val TAG = "PredictFoodActivity"
        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}
