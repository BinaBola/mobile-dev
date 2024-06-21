package com.binabola.app.presentation.strenghexercise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.lifecycleScope
import com.binabola.app.presentation.MainViewModel
import com.binabola.app.presentation.StrengthExercise.StrengthExercise
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.exercise.DetailExerciseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StrenghExerciseActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_strengh_exercise)
        val id = intent.getStringExtra(DetailExerciseActivity.EXTRA_ID) ?: ""
        println("ID: $id")

        setContent {
            MaterialTheme {
                StrengthExercise(workoutId = id.toLong(), navigateToHome = {
                    lifecycleScope.launch {
                        viewModel.finishMission(id)
                    }
                })
            }
        }

    }
}