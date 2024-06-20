package com.binabola.app.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetDetailExercise(

	@field:SerializedName("calorie_out")
	val calorieOut: Int? = null,

	@field:SerializedName("interactive_body_part_segment_value_id")
	val interactiveBodyPartSegmentValueId: Int? = null,

	@field:SerializedName("video")
	val video: String? = null,

	@field:SerializedName("is_support_interactive")
	val isSupportInteractive: Int? = null,

	@field:SerializedName("video_url")
	val videoUrl: Any? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("interactive_setting_id")
	val interactiveSettingId: Int? = null,

	@field:SerializedName("step")
	val step: String? = null,

	@field:SerializedName("body_part_needed")
	val bodyPartNeeded: String? = null,

	@field:SerializedName("submission")
	val submission: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
