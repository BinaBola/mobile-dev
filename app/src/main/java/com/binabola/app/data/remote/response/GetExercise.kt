package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetExercise(
	val exercises: List<GetExerciseItem?>? = null
//	@field:SerializedName("GetExercise")
//	val getExercise: List<GetExerciseItem?>? = null
)

data class GetExerciseItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("category")
	val category: String? = null
)
