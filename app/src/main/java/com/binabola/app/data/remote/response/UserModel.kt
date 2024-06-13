package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserModel(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	val isLogin: Boolean = false
)
