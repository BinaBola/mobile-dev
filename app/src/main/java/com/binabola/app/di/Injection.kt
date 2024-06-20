package com.binabola.app.di

import android.content.Context
import com.binabola.app.data.pref.UserPreference
import com.binabola.app.data.pref.dataStore
import com.binabola.app.data.remote.retrofit.ApiConfig
import com.binabola.app.data.repository.ExerciseRepository
import com.binabola.app.data.repository.PredictFoodRepository
import com.binabola.app.data.repository.UserRepository
import com.binabola.app.presentation.BinaBolaModule
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
        return ExerciseRepository.getInstance(pref,apiService)
    }
    fun providePredictRepository(context: Context): PredictFoodRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val mlRetrofit = BinaBolaModule().mlApi()
        val mlApiService = BinaBolaModule().mlApiService(mlRetrofit)
        val apiService = ApiConfig.getApiService(user.userId.toString())
        return PredictFoodRepository(pref,mlApiService,apiService)
    }
}