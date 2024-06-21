package com.binabola.app.data.repository

import android.util.Log
import com.binabola.app.data.Result
import com.binabola.app.data.local.FakeData
import com.binabola.app.data.model.Exercise
import com.binabola.app.data.pref.UserPreference
import com.binabola.app.data.remote.model.AllExerciseRespone
import com.binabola.app.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AllExerciseRepository @Inject constructor(private val exerciseApiService: ApiService, private val userPreference: UserPreference){

    private val exercises = mutableListOf<Exercise>()


    init {
        if (exercises.isEmpty()) {
            FakeData.fakeExerciseData.forEach { exercise ->
                exercises.add(
                    Exercise(
                        id = exercise.id,
                        name = exercise.name,
                        level = exercise.level,
                        calorie_out = exercise.calorie_out,
                        requiredEquiment = exercise.requiredEquiment,
                        detail = exercise.detail,
                        duration = exercise.duration,
                        step = exercise.step,
                        category = exercise.category,
                        isSupportInteractive = exercise.isSupportInteractive,
                        interactiveSetting = exercise.interactiveSetting,
                        interctiveBodyPartSegmentValue = exercise.interctiveBodyPartSegmentValue,
                        bodyPartNeeded = exercise.bodyPartNeeded,
                        photo = exercise.photo,
                        video = exercise.video
                    )
                )
            }
        }
    }

    fun getExerciseById(exerciseId : Long): Flow<Result<Exercise>> = flow{
        emit(Result.Loading)
        try{
//            val user = withContext(Dispatchers.IO) {
//                userPreference.getSession().firstOrNull()?.userId?.toString() ?: ""
//            }
//
//            val detailExerciseResponse = exerciseApiService.getAllExercise(exerciseId.toString(), user, exerciseId.toString())
            val data = exercises.first { it.id == exerciseId }
            Log.d("All Ex Repository","Get exercise: $data")
            emit(Result.Success(data))

        } catch (e: HttpException) {
            e.printStackTrace()
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AllExerciseRespone::class.java)
            errorResponse.message?.let { Result.Error(it) }?.let { emit(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }
}