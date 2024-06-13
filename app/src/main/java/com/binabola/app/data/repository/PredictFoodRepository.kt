package com.binabola.app.data.repository

import android.util.Log
import com.binabola.app.data.remote.response.PredictFoodResponse
import com.binabola.app.data.remote.retrofit.MlApiService
import com.binabola.app.data.util.UiState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import javax.inject.Inject

class PredictFoodRepository @Inject constructor(private val mlApiService: MlApiService) {



    fun predict(image : File)  : Flow<UiState<PredictFoodResponse>> = flow {
        emit(UiState.Loading)
        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            image.name,
            requestImageFile
        )

        try{
            val predictImageRespnse = mlApiService.predictImage(multipartBody)

            Log.d("WHEN PRDICT IMAGE " , predictImageRespnse.toString())
            emit(UiState.Success(predictImageRespnse))

        } catch (e: HttpException) {


            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PredictFoodResponse::class.java)

            errorResponse.status?.message?.let { UiState.Error(it) }?.let { emit(it) }
        }
    }
}