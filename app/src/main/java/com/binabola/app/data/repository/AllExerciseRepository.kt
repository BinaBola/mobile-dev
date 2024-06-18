package com.binabola.app.data.repository

import com.binabola.app.data.Result
import com.binabola.app.data.local.FakeData
import com.binabola.app.data.model.Exercise
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AllExerciseRepository @Inject constructor(private val exerciseApiService: ){

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

    fun getWorkoutByIdMuscle(muscleId: Int): Flow<Result<ExerciseResponse>> = flow  {
        emit(Result.Loading)
        try {
            val muscleResponse = exerciseApiService.getExerciseList(muscleId)
            emit(Result.Success(muscleResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ExerciseResponse::class.java)
            errorResponse.message?.let { Result.Error(it) }?.let { emit(it) }
        }

    }

    fun getAllMuscleCategory(): Flow<Result<MuscleResponse>> = flow {
        emit(Result.Loading)
        try{
            val muscleResponse = exerciseApiService.getMuscleList()
            emit(Result.Success(muscleResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, MuscleResponse::class.java)
            errorResponse.message?.let { Result.Error(it) }?.let { emit(it) }
        }
    }
    fun getExerciseById(workoutId: Long): Flow<Result<DetailExerciseRespone>> = flow{
        emit(Result.Loading)
        try{
            val detailExerciseResponse = exerciseApiService.getDetailExercise(workoutId.toInt())
            emit(Result.Success(detailExerciseResponse))

        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailExerciseRespone::class.java)
            errorResponse.message?.let { Result.Error(it) }?.let { emit(it) }
        }
    }
}