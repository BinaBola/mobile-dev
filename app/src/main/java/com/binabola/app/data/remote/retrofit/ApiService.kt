package com.binabola.app.data.remote.retrofit

import com.binabola.app.data.remote.response.DetailUserResponse
import com.binabola.app.data.remote.response.GetDetailExercise
import com.binabola.app.data.remote.response.GetExercise
import com.binabola.app.data.remote.response.GetExerciseItem
import com.binabola.app.data.remote.response.UserModel
import com.binabola.app.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String,
        @Field("name") nama: String,
        @Field("height") tb: String,
        @Field("weight") bb: String,
        @Field("birth_date") tglLahir: String,
        @Field("gender") gender: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ) : Call<UserModel>

    @GET("user/{id}")
    fun getDetail(
        @Path("id") id: String
    ) : Call<DetailUserResponse>

    @GET("exercises")
    fun getExercises() : Call<List<GetExerciseItem>>

    @GET("exercise/{id}")
    fun getDetailExercise(
        @Path("id") id: String
    ) : Call<GetDetailExercise>
}