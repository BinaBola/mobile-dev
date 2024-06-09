package com.binabola.app.data.remote.retrofit

import com.binabola.app.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("role") role: String,
        @Field("nama_lengkap") nama: String,
        @Field("tb") tb: String,
        @Field("bb") bb: String,
        @Field("tgl_lahir") tglLahir: String,
        @Field("gender") gender: String,
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    )
}