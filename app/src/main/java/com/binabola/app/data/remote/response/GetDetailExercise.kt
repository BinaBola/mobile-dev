package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetDetailExercise(

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("calorie_out")
	val calorieOut: Int? = null,

	@field:SerializedName("step")
	val step: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("video")
	val video: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)
