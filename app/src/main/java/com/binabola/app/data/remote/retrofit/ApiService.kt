package com.binabola.app.data.remote.retrofit

import com.binabola.app.data.remote.model.AllExerciseRespone
import com.binabola.app.data.remote.response.DefaultResponse
import com.binabola.app.data.remote.response.DetailUserResponse
import com.binabola.app.data.remote.response.GetDailyCalorieItem
import com.binabola.app.data.remote.response.GetDetailExercise
import com.binabola.app.data.remote.response.GetExerciseItem
import com.binabola.app.data.remote.response.UserModel
import com.binabola.app.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Path("id") id: String,
        @Query("user_id") uid: String,
        @Query("exercise_id") eid: String,
    ) : Call<GetDetailExercise>

    @GET("/exercise/{id}")
    suspend fun getAllExercise(
//        @Query("exercise_id") id: Int
        @Path("id") id: String,
        @Query("user_id") uid: String,
        @Query("exercise_id") eid: String,
    ): AllExerciseRespone
  
    @GET("calories/{userID}/{date}")
    fun getDailyCalories(
        @Path("userID") userID: String,
        @Path("date") date: String
    ) : Call<List<GetDailyCalorieItem>>

    @FormUrlEncoded
    @POST("startmission")
    fun startMission(
        @Field("user_id") uid: String,
        @Field("exercise_id") eid: String,
    ) : Call<DefaultResponse>

    @FormUrlEncoded
    @POST("finishmission")
    fun finishMission(
        @Field("user_id") uid: String,
        @Field("exercise_id") eid: String,
    ) : Call<DefaultResponse>

    @FormUrlEncoded
    @POST("submission")
    fun uploadLink(
        @Field("user_id") uid: String,
        @Field("exercise_id") eid: String,
        @Field("video_url") link: String,
    ) : Call<DefaultResponse>

    @FormUrlEncoded
    @POST("calories")
    suspend fun submitFood(
        @Field("user_id") userId: String,
        @Field("date") date: String,
        @Field("foods") foods: String,
        @Field("calorie") calorie: String,
        @Field("amount") amount: String,
        @Field("category") category: String,
    ) : DefaultResponse
}