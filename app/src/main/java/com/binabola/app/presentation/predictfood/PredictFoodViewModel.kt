package com.binabola.app.presentation.predictfood

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binabola.app.data.remote.response.PredictFoodResponse
import com.binabola.app.data.repository.PredictFoodRepository
import com.binabola.app.data.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class PredictFoodViewModel @Inject constructor(private val repository : PredictFoodRepository) : ViewModel() {
    private val _predictResult: MutableStateFlow<UiState<PredictFoodResponse>> =
        MutableStateFlow(UiState.Loading)
    val predict: StateFlow<UiState<PredictFoodResponse>>
        get() = _predictResult

    fun predictImage(file : File) {
        viewModelScope.launch {
            repository.predict(file).catch {exception ->
                _predictResult.value = UiState.Error(exception.toString())

            }.collect{ data ->
                _predictResult.value = data


            }
        }
    }
    fun onTakePhoto(uri : Uri) {
        Log.d("Bitmap", uri.toString())
    }
}