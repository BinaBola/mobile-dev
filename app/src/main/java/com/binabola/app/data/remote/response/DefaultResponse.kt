package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class DefaultResponse(

	@field:SerializedName("message")
	val message: String? = null
)
