package com.pixelrabbit.oculi.domain.models

data class UserProgress(
    val totalExercises: Int = 0,
    val totalTime: Long = 0,
    val currentStreak: Int = 0,
    val todayExercises: Int = 0,
    // Добавлено для корректного маппинга и логики сброса в репозитории
    val lastActivityDate: Long = 0
)