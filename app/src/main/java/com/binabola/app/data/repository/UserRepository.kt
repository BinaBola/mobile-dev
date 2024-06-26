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
import com.binabola.app.data.pref.UserPreference
import com.binabola.app.data.remote.response.DefaultResponse
import com.binabola.app.data.remote.response.DetailUserResponse
import com.binabola.app.data.remote.response.GetDailyCalorieItem
import com.binabola.app.data.remote.response.UserModel

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
) {
    private val regisresult = MediatorLiveData<Result<RegisterResponse>>()
    private val loginresult = MediatorLiveData<Result<UserModel>>()
    private val detailProfil = MediatorLiveData<Result<DetailUserResponse>>()
    private val dailyCalories = MediatorLiveData<Result<List<GetDailyCalorieItem>>>()
    private val totalCalorie = MediatorLiveData<Double>()

    fun register(mapdata: Map<String,String?>): LiveData<Result<RegisterResponse>> {
        regisresult.value = Result.Loading
        try{
            var username = mapdata["email"].toString().split("@")[0]
            val client = apiService.register(
                username,
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
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        regisresult.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    t.printStackTrace()
                    regisresult.value = Result.Error(t.message.toString())
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
            regisresult.value = Result.Error(e.toString())
        }

        return regisresult
    }

    fun login(email: String, password: String): LiveData<Result<UserModel>> {
        loginresult.value = Result.Loading
        try {
            val client = apiService.login(email, password)
            client.enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    if(response.isSuccessful) {
                        val body = response.body()
                        loginresult.value = Result.Success(body!!)
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        loginresult.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    t.printStackTrace()
                    loginresult.value = Result.Error(t.message.toString())
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
            loginresult.value = Result.Error(e.toString())
        }

        return loginresult
    }

    fun getProfile(uid: String): LiveData<Result<DetailUserResponse>> {
        detailProfil.value = Result.Loading

        try{
            val client = apiService.getDetail(uid)
            client.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if(response.isSuccessful) {
                        val body = response.body()
                        detailProfil.value = Result.Success(body!!)
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        detailProfil.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    t.printStackTrace()
                    detailProfil.value = Result.Error(t.message.toString())
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
            detailProfil.value = Result.Error(e.toString())
        }

        return detailProfil
    }

    fun getDailyCalories(uid: String, date: String): LiveData<Result<List<GetDailyCalorieItem>>> {
        dailyCalories.value = Result.Loading

        try{
            val client = apiService.getDailyCalories(uid, date)
            client.enqueue(object : Callback<List<GetDailyCalorieItem>> {
                override fun onResponse(
                    call: Call<List<GetDailyCalorieItem>>,
                    response: Response<List<GetDailyCalorieItem>>
                ) {
                    if(response.isSuccessful) {
                        val body = response.body()
                        dailyCalories.value = Result.Success(body!!)

                        totalCalorie.value = body.sumOf {
                            it.total?.toDouble() ?: 0.0
                        }
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        dailyCalories.value = Result.Error(errorMessage.toString())
                        totalCalorie.value = 0.0
                    }
                }

                override fun onFailure(call: Call<List<GetDailyCalorieItem>>, t: Throwable) {
                    t.printStackTrace()
                    dailyCalories.value = Result.Error(t.message.toString())
                    totalCalorie.value = 0.0
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
            dailyCalories.value = Result.Error(e.toString())
        }

        return dailyCalories
    }

    fun getTotalCalorie(): LiveData<Double> {
        return totalCalorie
    }

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