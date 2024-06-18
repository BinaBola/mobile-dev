package com.binabola.app.data.model

import androidx.annotation.DrawableRes

data class Exercise(
    val id: Long,
    val name: String,
    val level: Int,
    val calorie_out: Int,
    val requiredEquiment: Boolean,
    val detail: String,
    val duration: String,
    val step: Array<String>,
    val category: Category,
    val isSupportInteractive: Boolean = false,
    val interactiveSetting: InteractiveExerciseSetting = InteractiveExerciseSetting(
        repetion = 0,
        set = 0,
        RestInterval = 0
    ),
    val interctiveBodyPartSegmentValue: BodyPartSegmentValue = BodyPartSegmentValue(0.0, 0.0, 0.0, 0.0),
    val bodyPartNeeded: Array<String> = arrayOf(""),
    @DrawableRes val photo: Int,
    @DrawableRes val video: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Exercise

        if (id != other.id) return false
        if (name != other.name) return false
        if (level != other.level) return false
        if (calorie_out != other.calorie_out) return false
        if (requiredEquiment != other.requiredEquiment) return false
        if (detail != other.detail) return false
        if (!step.contentEquals(other.step)) return false
        if (category != other.category) return false
        if (isSupportInteractive != other.isSupportInteractive) return false
        if (interactiveSetting != other.interactiveSetting) return false
        if (interctiveBodyPartSegmentValue != other.interctiveBodyPartSegmentValue) return false
        if (!bodyPartNeeded.contentEquals(other.bodyPartNeeded)) return false
        if (photo != other.photo) return false
        if (video != other.video) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + level
        result = 31 * result + calorie_out
        result = 31 * result + requiredEquiment.hashCode()
        result = 31 * result + detail.hashCode()
        result = 31 * result + step.contentHashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + isSupportInteractive.hashCode()
        result = 31 * result + interactiveSetting.hashCode()
        result = 31 * result + interctiveBodyPartSegmentValue.hashCode()
        result = 31 * result + bodyPartNeeded.contentHashCode()
        result = 31 * result + photo
        result = 31 * result + video
        return result
    }
}
data class BodyPartSegmentValue(
    val rightArm: Double,
    val leftArm: Double,
    val rightLeg: Double,
    val leftLeg: Double,
)
data class InteractiveExerciseSetting(
    val repetion: Int,
    val set: Int,
    val RestInterval: Int,
)

