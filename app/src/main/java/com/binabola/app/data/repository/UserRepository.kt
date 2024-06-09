package com.binabola.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.binabola.app.data.remote.response.RegisterResponse
import com.binabola.app.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.binabola.app.data.Result
import com.binabola.app.data.pref.UserModel
import com.binabola.app.data.pref.UserPreference

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    private val regisresult = MediatorLiveData<Result<RegisterResponse>>()
//    private val loginresult = MediatorLiveData<Result<LoginResponse>>()

    fun register(mapdata: Map<String,String>): LiveData<Result<RegisterResponse>> {
        regisresult.value = Result.Loading
        try{
            val client = apiService.register(
                "",
                mapdata["email"].toString(),
                mapdata["password"].toString(),
                mapdata["role"].toString(),
                mapdata["nama"].toString(),
                mapdata["tb"].toString(),
                mapdata["bb"].toString(),
                mapdata["dob"].toString(),
                mapdata["gender"].toString(),
            )
            client.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        regisresult.value = Result.Success(body!!)
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                        val errorMessage = errorBody.message

                        regisresult.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    regisresult.value = Result.Error(t.message.toString())
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
            regisresult.value = Result.Error(e.toString())
        }

        return regisresult
    }

//    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
//        loginresult.value = Result.Loading
//        try {
//            val client = apiService.login( email, password)
//            client.enqueue(object : Callback<LoginResponse> {
//                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                    if(response.isSuccessful) {
//                        val body = response.body()
//                        loginresult.value = Result.Success(body!!)
//                    } else {
//                        val jsonInString = response.errorBody()?.string()
//                        val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
//                        val errorMessage = errorBody.message
//
//                        loginresult.value = Result.Error(errorMessage.toString())
//                    }
//                }
//
//                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    loginresult.value = Result.Error(t.message.toString())
//                }
//
//            })
//        } catch (e: Exception) {
//            e.printStackTrace()
//            loginresult.value = Result.Error(e.toString())
//        }
//
//        return loginresult
//    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        fun getInstance(userPreference: UserPreference, apiService: ApiService) = UserRepository(userPreference,apiService)
    }
}