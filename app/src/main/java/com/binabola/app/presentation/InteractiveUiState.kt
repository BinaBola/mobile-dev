package com.binabola.app.presentation

data class InteractiveUiState(
    val exerciseId : Long = 0 ,
    val isTutorialScreen : Boolean = true,
    val tutorialStep : Int = 1,
    val score : Int = 0,
    val counter : Int = 0,
    val isFinish : Boolean = false,
    val findingObject : Boolean = true,
    val currentSet : Int = 0,
    val isInRestMode  : Boolean = false,
    val isFinished : Boolean = false,
)
