package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetExerciseItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("calorie_out")
	val calorieOut: Double? = null,

	@field:SerializedName("foto")
	val foto: String? = null,
)
