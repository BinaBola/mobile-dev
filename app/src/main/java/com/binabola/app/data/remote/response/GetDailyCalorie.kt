package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetDailyCalorieItem(

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("foods")
	val foods: String? = null
)
