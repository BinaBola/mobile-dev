package com.binabola.app.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
//                RegisterViewModel() as T
//            }
//            else ->
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory = ViewModelFactory()
    }
}