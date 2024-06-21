package com.binabola.app.data.repository

import android.util.Log
import com.binabola.app.data.Result
import com.binabola.app.data.pref.UserPreference
import com.binabola.app.data.remote.response.DefaultResponse
import com.binabola.app.data.remote.response.PredictFoodResponse
import com.binabola.app.data.remote.response.PredictionData
import com.binabola.app.data.remote.retrofit.ApiService
import com.binabola.app.data.remote.retrofit.MlApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.HttpException
import javax.inject.Inject

class PredictFoodRepository @Inject constructor(private val userPreference: UserPreference, private val mlApiService: MlApiService, private val apiService: ApiService) {

    fun predict(image: MultipartBody.Part): Flow<Result<PredictionData>> = flow {
        emit(Result.Loading)
//        val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
//        val multipartBody = MultipartBody.Part.createFormData(
//            "image",
//            image.name,
//            requestImageFile
//        )

        try {
            val predictImageResponse = mlApiService.predictImage(image)
            Log.d("PredictImage", predictImageResponse.toString())
            emit(Result.Success(predictImageResponse))
        } catch (e: HttpException) {
            e.printStackTrace()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, PredictFoodResponse::class.java)
            val errorMessage = errorResponse.status?.message ?: "Unknown error"
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    suspend fun submitFood(
        date: String,
        foods: String,
        calorie: String,
        amount: String,
        category: String,
    ) : Flow<Result<DefaultResponse>> = flow {
        emit(Result.Loading)
        val user = withContext(Dispatchers.IO) {
            userPreference.getSession().firstOrNull()?.userId?.toString() ?: ""
        }

        try{
            val submitResponse = apiService.submitFood(
                user,
                date,
                foods,
                calorie,
                amount,
                category
            )
            Log.d("PredictImage", submitResponse.toString())
            emit(Result.Success(submitResponse))
        } catch (e: HttpException) {
            e.printStackTrace()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DefaultResponse::class.java)
            val errorMessage = errorResponse.message ?: "Unknown error"
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }
}
