package com.binabola.app

import com.binabola.app.data.model.ApiConstant
import com.binabola.app.data.remote.retrofit.MlApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

class BinaBolaModule {

    @Singleton
    @MlApi
    fun mlApi(

    )  : Retrofit {

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
        val client = clientBuilder.build()


        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ApiConstant.ML_BASE_URL).build()
    }
    @Singleton
    fun mlApiService(@MlApi retrofit : Retrofit) : MlApiService = retrofit.create(MlApiService::class.java)

}
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MlApi