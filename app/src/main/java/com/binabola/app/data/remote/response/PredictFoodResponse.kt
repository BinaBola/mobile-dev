package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictFoodResponse(
    @field:SerializedName("data")
    val data: PredictionData? = null,

    @field:SerializedName("status")
    val status: Status? = null
)

data class PredictionData(
    @field:SerializedName("Calories")
    val calories: Double? = null,

    @field:SerializedName("Carbs")
    val carbs: Double? = null,

    @field:SerializedName("Confidence")
    val confidence: Double? = null,

    @field:SerializedName("Fat")
    val fat: Double? = null,

    @field:SerializedName("Food Prediction")
    val foodPrediction: String? = null,

    @field:SerializedName("Protein")
    val protein: String? = null
)

data class Status(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)
