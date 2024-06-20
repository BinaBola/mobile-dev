package com.binabola.app.presentation.predictfood

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binabola.app.data.remote.response.PredictFoodResponse
import com.binabola.app.data.repository.PredictFoodRepository
import com.binabola.app.data.Result
import com.binabola.app.data.remote.response.DefaultResponse
import com.binabola.app.data.remote.response.PredictionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class PredictFoodViewModel @Inject constructor(private val repository: PredictFoodRepository) : ViewModel() {

    private val _predictResult: MutableStateFlow<Result<PredictionData>> = MutableStateFlow(Result.Loading)
    val predict: StateFlow<Result<PredictionData>> get() = _predictResult
    private val _submitResult: MutableStateFlow<Result<DefaultResponse>> = MutableStateFlow(Result.Loading)
    val submitResult: StateFlow<Result<DefaultResponse>> get() = _submitResult

    suspend fun predictImage(file: MultipartBody.Part) {
        viewModelScope.launch {
            repository.predict(file)
                .catch { exception ->
                    _predictResult.value = Result.Error(exception.message ?: "Unknown error")
                }
                .collect { data ->
                    Log.d("PredictViewModel","success: $data")
                    _predictResult.value = data
                }
        }
    }

    suspend fun submitFood(
        date: String,
        foods: String,
        calorie: String,
        amount: String,
        category: String,
        ) {
        repository.submitFood(
            date,foods,calorie,amount,category
        ).catch {exception->
            _submitResult.value = Result.Error(exception.message ?: "Unknown error")
        }
            .collect { data ->
                Log.d("PredictViewModel","success: $data")
                _submitResult.value = data
            }
    }
}
