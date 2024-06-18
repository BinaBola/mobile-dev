package com.binabola.app.presentation.predictfood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binabola.app.data.remote.response.PredictFoodResponse
import com.binabola.app.data.repository.PredictFoodRepository
import com.binabola.app.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class PredictFoodViewModel @Inject constructor(private val repository: PredictFoodRepository) : ViewModel() {

    private val _predictResult: MutableStateFlow<Result<PredictFoodResponse>> = MutableStateFlow(Result.Loading)
    val predict: StateFlow<Result<PredictFoodResponse>> get() = _predictResult

    fun predictImage(file: File) {
        viewModelScope.launch {
            repository.predict(file)
                .catch { exception ->
                    _predictResult.value = Result.Error(exception.message ?: "Unknown error")
                }
                .collect { data ->
                    _predictResult.value = data
                }
        }
    }
}
