package com.binabola.app.presentation.exercise

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.data.remote.response.GetDetailExercise
import com.binabola.app.databinding.ActivityDetailExerciseBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.MainViewModel
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.predictfood.PredictFoodActivity
import com.binabola.app.presentation.predictfood.PredictFoodActivity.Companion

import com.binabola.app.presentation.strenghexercise.StrenghExerciseActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import java.util.Locale

class DetailExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailExerciseBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var id : String

    private val cameracode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra(EXTRA_ID) ?: ""
        println("ID: $id")
        requestPermissions()

//        binding.btnAction.setOnClickListener {
//            startMission(id)
//        }

        lifecycleScope.launch {
            viewModel.getDetailExercise(id).observe(this@DetailExerciseActivity) { data ->
                when (data) {
                    is Result.Loading -> {}
                    is Result.Error -> {
                        AppUtil().showToast(this@DetailExerciseActivity, data.error)
                    }
                    is Result.Success -> {
                        println(data.data)
                        binding.tvOverview.text = data.data.name
                        binding.tvDetail.text = data.data.detail

                        if((data.data.status ?: 0) == 0) {
                            binding.btnAction.text = "Mulai Latihan"
                            binding.btnAction.setOnClickListener {

                                startMission(data.data.id.toString(), data.data.category.toString())
                            }
                        } else if((data.data.status ?: 0) == 1) {
                            binding.btnAction.text = "Selesaikan Latihan"
                            binding.btnAction.setOnClickListener {
                                finishMission(data.data.id.toString())
                            }
                            if(data.data.category.toString().lowercase(Locale.getDefault()) == "strength") {
                                binding.btnAction.text = "Lanjutkan Latihan"
                                binding.btnAction.setOnClickListener {
                                    val intent = Intent(this@DetailExerciseActivity, StrenghExerciseActivity::class.java)
                                    intent.putExtra(EXTRA_ID, id)
                                    startActivity(intent)
                                }
                            } else if(data.data.category.toString().lowercase(Locale.getDefault()) == "endurance") {
                                binding.btnAction.text = "Lanjutkan Latihan"
                                binding.btnAction.setOnClickListener {
                                    val intent = Intent(
                                        this@DetailExerciseActivity,
                                        TrackRunActivity::class.java
                                    )
                                    intent.putExtra(EXTRA_ID, id)
                                    startActivity(intent)
                                }
                            }
                        } else {
                            binding.btnAction.isEnabled = false
                            binding.btnAction.text = "Latihan selesai"
                            binding.btnAction.setBackgroundColor(resources.getColor(R.color.grey_600))
                        }

                        if(data.data.submission == 0) {
                            binding.tabLayout.addTab(
                                binding.tabLayout.newTab().setText("Deskripsi")
                            )
                            binding.tabLayout.addTab(
                                binding.tabLayout.newTab().setText("Step By Step")
                            )
                        } else {
                            binding.tabLayout.addTab(
                                binding.tabLayout.newTab().setText("Deskripsi")
                            )
                            binding.tabLayout.addTab(
                                binding.tabLayout.newTab().setText("Step By Step")
                            )
                            binding.tabLayout.addTab(
                                binding.tabLayout.newTab().setText("Upload")
                            )
                        }

                        setupTab(data.data)

                        Glide.with(binding.root.context).load(data.data.foto).placeholder(R.drawable.placeholder).into(binding.imgExercise)
                    }
                }
            }
        }


    }

    private fun requestPermissions() {
        val permissions = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }
        
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), cameracode)
        }
    }


    private fun setupTab(data: GetDetailExercise) {
        binding.tabLayout.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.tvOverview.text = data.name
                        binding.tvDetail.text = data.detail

                        binding.btnAction.visibility = View.VISIBLE
                        binding.btnUpload.visibility = View.GONE
                        binding.edtLink.visibility = View.GONE
                    }
                    1 -> {
                        binding.tvOverview.text = data.name
                        binding.tvDetail.text = data.step?.let { StepByStepContent(it) }

                        binding.btnAction.visibility = View.VISIBLE
                        binding.btnUpload.visibility = View.GONE
                        binding.edtLink.visibility = View.GONE

                    }
                    else -> {
                        binding.tvOverview.text = "Upload Tugas Video"
                        binding.tvDetail.text = "Silakan cantumkan link video tugas di bawah ini. Link video dapat berupa link Youtube, Google Drive, atau link postingan sosial media"

                        binding.btnAction.visibility = View.GONE
                        binding.btnUpload.visibility = View.VISIBLE
                        binding.edtLink.visibility = View.VISIBLE

                        if(data.videoUrl != null) {
                            binding.edtLink.setText(data.videoUrl.toString())
                            binding.edtLink.inputType = InputType.TYPE_NULL
                            binding.btnUpload.isEnabled = false
                            binding.btnUpload.setBackgroundColor(resources.getColor(R.color.grey_600))
                            binding.btnUpload.text = "Tugas Selesai"
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.btnUpload.setOnClickListener {
            uploadTugas()
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun StepByStepContent(step: String): String {
        val stepsArray = step.split(". ").toTypedArray()
        return formatSteps(stepsArray)
    }

    private fun formatSteps(steps: Array<String>): String {
        val formattedSteps = StringBuilder()

        for ((index, step) in steps.withIndex()) {
            formattedSteps.append("${index + 1}. $step\n")
        }

        return formattedSteps.toString().trimEnd()
    }

    private fun startMission(id:String, category: String) {
        lifecycleScope.launch {
            viewModel.startMission(id).observe(this@DetailExerciseActivity) {
                when (it) {
                    is Result.Loading -> {}
                    is Result.Error -> {
                        AppUtil().showToast(this@DetailExerciseActivity, it.error)
                    }
                    is Result.Success -> {
                        if(category.lowercase(Locale.getDefault()) == "strength") {
                            val intent = Intent(
                                this@DetailExerciseActivity,
                                StrenghExerciseActivity::class.java
                            )
                            intent.putExtra(EXTRA_ID, id)
                            startActivity(intent)
                        } else if (category.lowercase(Locale.getDefault()) == "endurance") {
                            val intent = Intent(
                                this@DetailExerciseActivity,
                                TrackRunActivity::class.java
                            )
                            intent.putExtra(EXTRA_ID, id)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun finishMission(uid:String) {
        lifecycleScope.launch {
            viewModel.finishMission(uid).observe(this@DetailExerciseActivity) {
                when (it) {
                    is Result.Loading -> {}
                    is Result.Error -> {
                        AppUtil().showToast(this@DetailExerciseActivity, it.error)
                    }
                    is Result.Success -> {
                        AppUtil().showToast(this@DetailExerciseActivity, it.data.message.toString())
                        finish()
                    }
                }
            }
        }
    }

    private fun uploadTugas() {
        val link = binding.edtLink.text.toString()

        if(link.isEmpty()) {
            AppUtil().showToast(this@DetailExerciseActivity, "Link video tidak boleh kosong ya")
            return
        }

        if(!link.startsWith("http")) {
            AppUtil().showToast(this@DetailExerciseActivity, "Link video tidak valid")
            return
        }

        lifecycleScope.launch {
            viewModel.uploadLink(id, link).observe(this@DetailExerciseActivity) {
                when(it) {
                    is Result.Loading -> {}
                    is Result.Error -> {
                        AppUtil().showToast(this@DetailExerciseActivity, it.error)
                    }
                    is Result.Success -> {
                        AppUtil().showToast(this@DetailExerciseActivity, it.data.message.toString())
                        finish()
                    }
                }
            }
        }
    }

    companion object{
        const val EXTRA_ID = "EXTRA_ID"
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}
