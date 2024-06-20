package com.binabola.app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.binabola.app.data.Result
import com.binabola.app.data.pref.UserPreference
import com.binabola.app.data.remote.response.DefaultResponse
import com.binabola.app.data.remote.response.GetDetailExercise
import com.binabola.app.data.remote.response.GetExerciseItem
import com.binabola.app.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class ExerciseRepository private constructor(
    private val userPref: UserPreference,
    private val apiService: ApiService,
){
    private val listExercise = MediatorLiveData<Result<List<GetExerciseItem>>>()
    private val detailExercise = MediatorLiveData<Result<GetDetailExercise>>()
    private val defaultResponse = MediatorLiveData<Result<DefaultResponse>>()

    fun getExercises(): LiveData<Result<List<GetExerciseItem>>> {
        listExercise.value = Result.Loading

        try{
            val client = apiService.getExercises()
            client.enqueue(object : Callback<List<GetExerciseItem>>{
                override fun onResponse(call: Call<List<GetExerciseItem>>, response: Response<List<GetExerciseItem>>) {
                    if (response.isSuccessful) {
                        listExercise.value = Result.Success(response.body()!!)
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        listExercise.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<List<GetExerciseItem>>, t: Throwable) {
                    t.printStackTrace()
                    listExercise.value = Result.Error(t.message.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            listExercise.value = Result.Error(e.toString())
        }

        return listExercise
    }

    suspend fun getDetailExercise(id: String): LiveData<Result<GetDetailExercise>> {
        detailExercise.value = Result.Loading
        val user = withContext(Dispatchers.IO) {
            userPref.getSession().firstOrNull()?.userId?.toString() ?: ""
        }

        try{
            val client = apiService.getDetailExercise(id,user,id)
            client.enqueue(object : Callback<GetDetailExercise>{
                override fun onResponse(call: Call<GetDetailExercise>, response: Response<GetDetailExercise>) {
                    if (response.isSuccessful) {
                        detailExercise.value = Result.Success(response.body()!!)
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        detailExercise.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<GetDetailExercise>, t: Throwable) {
                    t.printStackTrace()
                    detailExercise.value = Result.Error(t.message.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            detailExercise.value = Result.Error(e.toString())
        }

        return detailExercise
    }

    suspend fun startMission(id: String): LiveData<Result<DefaultResponse>> {
        defaultResponse.value = Result.Loading
        val user = withContext(Dispatchers.IO) {
            userPref.getSession().firstOrNull()?.userId?.toString() ?: ""
        }

        try{
            val client = apiService.startMission(user,id)
            client.enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        defaultResponse.value = Result.Success(response.body()!!)
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        defaultResponse.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    t.printStackTrace()
                    defaultResponse.value = Result.Error(t.message.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            defaultResponse.value = Result.Error(e.toString())
        }

        return defaultResponse
    }

    suspend fun finishMission(id: String): LiveData<Result<DefaultResponse>> {
        defaultResponse.value = Result.Loading
        val user = withContext(Dispatchers.IO) {
            userPref.getSession().firstOrNull()?.userId?.toString() ?: ""
        }

        try{
            val client = apiService.finishMission(user,id)
            client.enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        defaultResponse.value = Result.Success(response.body()!!)
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        defaultResponse.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    t.printStackTrace()
                    defaultResponse.value = Result.Error(t.message.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            defaultResponse.value = Result.Error(e.toString())
        }

        return defaultResponse
    }

    suspend fun uploadLink(id: String, link: String): LiveData<Result<DefaultResponse>> {
        defaultResponse.value = Result.Loading
        val user = withContext(Dispatchers.IO) {
            userPref.getSession().firstOrNull()?.userId?.toString() ?: ""
        }

        try{
            val client = apiService.uploadLink(user,id,link)
            client.enqueue(object : Callback<DefaultResponse>{
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        defaultResponse.value = Result.Success(response.body()!!)
                    } else {
                        val jsonInString = response.errorBody()?.string()
                        val errorBody = Gson().fromJson(jsonInString, DefaultResponse::class.java)
                        val errorMessage = errorBody.message

                        defaultResponse.value = Result.Error(errorMessage.toString())
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    t.printStackTrace()
                    defaultResponse.value = Result.Error(t.message.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            defaultResponse.value = Result.Error(e.toString())
        }

        return defaultResponse
    }



    companion object {
        fun getInstance(pref: UserPreference,apiService: ApiService) = ExerciseRepository(pref,apiService)
    }
}