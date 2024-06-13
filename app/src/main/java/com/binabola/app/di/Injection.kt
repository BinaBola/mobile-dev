package com.binabola.app.di

import android.content.Context
import com.binabola.app.data.pref.UserPreference
import com.binabola.app.data.pref.dataStore
import com.binabola.app.data.remote.retrofit.ApiConfig
import com.binabola.app.data.repository.ExerciseRepository
import com.binabola.app.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.userId.toString())
        return UserRepository.getInstance(pref,apiService)
    }
    fun provideExerciseRepository(context: Context): ExerciseRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.userId.toString())
        return ExerciseRepository.getInstance(apiService)
    }
}