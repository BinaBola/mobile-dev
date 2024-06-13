package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictFoodResponse(
    @field:SerializedName("data")
    val data: PredictionData? = null,

    @field:SerializedName("status")
    val status: Status? = null
)
data class PredictionData(

    @field:SerializedName("confidence")
    val confidence: Double? = null,

    @field:SerializedName("exercise")
    val calory: String? = null,

    @field:SerializedName("gym_equipment_prediction")
    val FoodPrediction: String? = null,


    )
data class Status(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)
