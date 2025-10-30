package com.pixelrabbit.oculi.domain.models

data class UserProgress(
    val userId: String,
    val totalSessions: Int,
    val totalDuration: Long, // в секундах
    val streak: Int,
    val lastActivity: String? = null,
    val achievements: List<Achievement> = emptyList()
)