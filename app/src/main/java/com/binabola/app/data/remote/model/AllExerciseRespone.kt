package com.binabola.app.data.remote.model

import com.binabola.app.data.model.Category
import com.google.gson.annotations.SerializedName

data class AllExerciseRespone(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: DetailExercise? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class InteractiveSetting(

    @field:SerializedName("set")
    val set: Int? = null,

    @field:SerializedName("rest_interval")
    val restInterval: Int? = null,

    @field:SerializedName("repetition")
    val repetition: Int? = null
)


data class InteractiveBodyPartSegmentValue(

    @field:SerializedName("rightLeg")
    val rightLeg: Int? = null,

    @field:SerializedName("rightArm")
    val rightArm: Int? = null,

    @field:SerializedName("leftArm")
    val leftArm: Int? = null,

    @field:SerializedName("leftLeg")
    val leftLeg: Int? = null
)

data class DetailExercise(

    @field:SerializedName("overview")
    val overview: String? = null,

    @field:SerializedName("level")
    val level: Int? = null,

    @field:SerializedName("required_equipment")
    val requiredEquipment: Int? = null,

    @field:SerializedName("interactive_setting")
    val interactiveSetting: InteractiveSetting? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("cal_estimation")
    val calEstimation: Int? = null,

    @field:SerializedName("gif_url")
    val gifUrl: String? = null,

    @field:SerializedName("is_support_interactive")
    val isSupportInteractive: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("interactive_body_part_segment_value")
    val interactiveBodyPartSegmentValue: InteractiveBodyPartSegmentValue? = null,

    @field:SerializedName("step")
    val step: String? = null,

    @field:SerializedName("body_part_needed")
    val bodyPartNeeded: List<String?>? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("photo_url")
    val photoUrl: String? = null,

    @field:SerializedName("category")
    val category: Category? = null
)
