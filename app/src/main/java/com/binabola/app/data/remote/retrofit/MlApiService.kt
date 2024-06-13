package com.binabola.app.data.remote.retrofit

import com.binabola.app.data.remote.response.PredictFoodResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MlApiService {
    @Multipart
    @POST("/prediction")
    suspend fun predictImage(
        @Part image: MultipartBody.Part,
    ): PredictFoodResponse
}