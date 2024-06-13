package com.binabola.app.presentation.exercise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.databinding.ActivityDetailExerciseBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.MainViewModel
import com.binabola.app.presentation.ViewModelFactory
import com.bumptech.glide.Glide

class DetailExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailExerciseBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        println("ID: $id")

        viewModel.getDetailExercise(id!!).observe(this) {
            when (it) {
                is Result.Loading -> {}
                is Result.Error -> {
                    AppUtil().showToast(this@DetailExerciseActivity, it.error)
                }
                is Result.Success -> {
                    println(it.data)
                    binding.tvOverview.text = it.data.name
                    binding.tvDetail.text = it.data.detail

                    Glide.with(binding.root.context).load(it.data.foto).placeholder(R.drawable.placeholder).into(binding.imgExercise)
                }
            }
        }
    }

    companion object{
        const val EXTRA_ID = "EXTRA_ID"
    }
}