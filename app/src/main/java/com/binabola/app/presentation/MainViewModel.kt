package com.binabola.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.binabola.app.data.remote.response.DetailUserResponse
import com.binabola.app.data.remote.response.GetDailyCalorieItem
import com.binabola.app.data.remote.response.UserModel
import com.binabola.app.data.repository.ExerciseRepository
import com.binabola.app.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository, private val exerciseRepository: ExerciseRepository) : ViewModel() {
    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> = _currentDate

    private val _profile = MutableLiveData<DetailUserResponse>()
    val profile: LiveData<DetailUserResponse> = _profile

    private val _listFood = MutableLiveData<List<GetDailyCalorieItem>>()
    val listFood: LiveData<List<GetDailyCalorieItem>> = _listFood

    fun setDate(newDate: String) {
        _currentDate.value = newDate
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getProfile(uid: String) = repository.getProfile(uid)
    fun setProfile(profile: DetailUserResponse) {
        viewModelScope.launch {
            _profile.value = profile
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getExercises() = exerciseRepository.getExercises()

    suspend fun getDetailExercise(id: String) = exerciseRepository.getDetailExercise(id)

    fun getDailyCalories(uid: String, date: String) = repository.getDailyCalories(uid, date)

    fun setListFood(listFood: List<GetDailyCalorieItem>) {
        viewModelScope.launch {
            _listFood.value = listFood
        }
    }

    fun getTotalCalories() = repository.getTotalCalorie()

    suspend fun startMission(id: String) = exerciseRepository.startMission(id)
    suspend fun finishMission(id: String) = exerciseRepository.finishMission(id)
    suspend fun uploadLink(id: String, link: String) = exerciseRepository.uploadLink(id,link)
}