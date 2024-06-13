package com.binabola.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.binabola.app.data.remote.response.DetailUserResponse
import com.binabola.app.data.remote.response.UserModel
import com.binabola.app.data.repository.ExerciseRepository
import com.binabola.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository, private val exerciseRepository: ExerciseRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getProfile(uid: String) = repository.getProfile(uid)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getExercises() = exerciseRepository.getExercises()

    fun getDetailExercise(id: String) = exerciseRepository.getDetailExercise(id)
}