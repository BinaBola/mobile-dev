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
import com.binabola.app.data.remote.response.GetExercise
import com.binabola.app.data.remote.response.GetExerciseItem
import com.binabola.app.data.remote.retrofit.ApiService
import com.google.gson.Gson

class ExerciseRepository private constructor(
    private val apiService: ApiService,
){
    private val listExercise = MediatorLiveData<Result<List<GetExerciseItem>>>()
    private val detailExercise = MediatorLiveData<Result<GetDetailExercise>>()

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

    fun getDetailExercise(id: String): LiveData<Result<GetDetailExercise>> {
        detailExercise.value = Result.Loading

        try{
            val client = apiService.getDetailExercise(id)
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

    companion object {
        fun getInstance(apiService: ApiService) = ExerciseRepository(apiService)
    }
}