package com.pixelrabbit.backy.domain.models

data class UserProgress(
    val totalExercises: Int = 0,
    val totalTime: Long = 0,
    val currentStreak: Int = 0,
    val todayExercises: Int = 0,
    val lastActivityDate: Long = 0
)