package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
