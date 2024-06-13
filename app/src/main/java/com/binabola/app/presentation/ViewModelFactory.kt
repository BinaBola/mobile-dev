package com.binabola.app.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.binabola.app.data.repository.ExerciseRepository
import com.binabola.app.data.repository.UserRepository
import com.binabola.app.di.Injection
import com.binabola.app.presentation.login.LoginViewModel
import com.binabola.app.presentation.register.RegisterViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val exerciseRepository: ExerciseRepository

) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, exerciseRepository) as T
            }
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory = ViewModelFactory(Injection.provideRepository(context), Injection.provideExerciseRepository(context))
    }
}