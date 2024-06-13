package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

	@field:SerializedName("bb")
	val bb: String? = null,

	@field:SerializedName("kalori")
	val kalori: String? = null,

	@field:SerializedName("umur")
	val umur: Int? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("nama_lengkap")
	val namaLengkap: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("tb")
	val tb: String? = null
)
