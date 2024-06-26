package com.binabola.app.data.remote.retrofit

import com.binabola.app.data.remote.response.PredictionData
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MlApiService {
    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Part image: MultipartBody.Part,
//    ): PredictFoodResponse
    ): PredictionData

}