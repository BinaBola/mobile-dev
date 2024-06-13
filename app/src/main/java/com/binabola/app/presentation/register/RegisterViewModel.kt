package com.binabola.app.presentation.register

import androidx.lifecycle.ViewModel
import com.binabola.app.data.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(mapData: Map<String, String?>) = repository.register(mapData)
}