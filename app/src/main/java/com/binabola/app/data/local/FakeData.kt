package com.binabola.app.data.local

import com.binabola.app.R
import com.binabola.app.data.model.BodyPartSegmentValue
import com.binabola.app.data.model.Category
import com.binabola.app.data.model.Exercise
import com.binabola.app.data.model.InteractiveExerciseSetting

object FakeData {

    private val fakeCategory = listOf(
        Category("Strength"),
        Category("Speed"),
        Category("Endurance"),

        )


    val fakeExerciseData = listOf(
        Exercise(
            id = 1,
            name = "Dumbbell Curl",
            detail = "The dumbbell curl is a bicep exercise that targets the muscles in your arms.",
            duration = "00:00:00",
            level = 1,
            category = fakeCategory[0],
            requiredEquiment = true,
            step = arrayOf(
                "Stand with your feet shoulder-width apart, holding a dumbbell in each hand with your arms fully extended and palms facing forward.",
                "Keep your upper arms stationary and exhale as you curl the weights while contracting your biceps.",
                "Inhale and slowly begin to lower the dumbbells back to the starting position."
            ),
            calorie_out = 500,
            photo = R.drawable.dumbbel_bicep_cover,
            video = R.drawable.dumbbel_bicep_cover,
            bodyPartNeeded = arrayOf("right_hand" , "left_hand" ),
            isSupportInteractive = true,
            interactiveSetting = InteractiveExerciseSetting(
                repetion = 12,
                set = 3,
                RestInterval = 60
            ),
            interctiveBodyPartSegmentValue = BodyPartSegmentValue(
                rightArm = 10.0,
                leftArm = 10.0,
                rightLeg = 0.0,
                leftLeg= 0.0,
            ),

        ),Exercise(
            id = 2,
            name = "Push-ups",
            detail = "Bodyweight exercise targeting the chest, shoulders, and triceps.",
            duration = "00:00:00",
            level = 2,
            category = fakeCategory[0],
            requiredEquiment = true,
            step = arrayOf(
                "Start in a plank position.",
                "Lower your body until your chest nearly touches the floor.",
                " Push back up to the starting position."),
            calorie_out = 400,
            photo = R.drawable.push_up,
            video = R.drawable.push_up,
            bodyPartNeeded = arrayOf("right_hand" , "left_hand" ),
            isSupportInteractive = true,
            interactiveSetting = InteractiveExerciseSetting(
                repetion = 6,
                set = 3,
                RestInterval = 60
            ),
            interctiveBodyPartSegmentValue = BodyPartSegmentValue(
                rightArm = 55.0,
                leftArm = 49.0,
                rightLeg = 0.0,
                leftLeg= 0.0,
            ),

            ),Exercise(
            id = 3,
            name = "Dumbbell Sumo Squat ",
            detail = "A variation of the traditional squat that targets the inner thighs and glutes.",
            duration = "00:00:00",
            level = 3,
            category = fakeCategory[0],
            requiredEquiment = true,
            step = arrayOf(
                "Stand with feet wider than shoulder-width, toes pointing out.",
                "Hold a dumbbell with both hands in front of you.",
                "Squat down, keeping your back straight.",
                "Return to the starting position."
            ),
            calorie_out = 150,
            photo = R.drawable.sumo_squat_cover,
            video =R.drawable.sumo_squat_cover,
            bodyPartNeeded = arrayOf("right_leg" , "left_leg" ),
            isSupportInteractive = true,
            interactiveSetting = InteractiveExerciseSetting(
                repetion = 10,
                set = 3,
                RestInterval = 60
            ),
            interctiveBodyPartSegmentValue = BodyPartSegmentValue(
                rightArm = 0.0,
                leftArm = 0.0,
                rightLeg = 110.6,
                leftLeg= 110.7,
            ),
                ),Exercise(
            id = 4,
            name = "Dumbbell Biceps Curl on Bosu Ball",
            detail = "An exercise that combines core stabilization with bicep strengthening.",
            duration = "00:00:00",
            level = 1,
            category = fakeCategory[0],
            requiredEquiment = true,
            step = arrayOf(
                "Sit on a Bosu ball in a V-sit position.",
                "Perform bicep curls while maintaining balance.",
                "Repeat"),
            calorie_out = 180,
            photo = R.drawable.dumbbel_bicep_cover,
            video = R.drawable.dumbbel_bicep_cover,
            bodyPartNeeded = arrayOf("right_hand" , "left_hand" ),
            isSupportInteractive = true,
            interactiveSetting = InteractiveExerciseSetting(
                repetion = 14,
                set = 3,
                RestInterval = 60
            ),
            interctiveBodyPartSegmentValue = BodyPartSegmentValue(
                rightArm = 10.0,
                leftArm = 10.0,
                rightLeg = 0.0,
                leftLeg= 0.0,
            ),

            )
    )
}