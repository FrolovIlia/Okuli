package com.pixelrabbit.oculi.domain.repositories

interface StatsRepository {
    suspend fun getTotalExercises(): Int
    suspend fun getTotalTime(): Int // в минутах
    suspend fun getCurrentStreak(): Int // дней подряд
    suspend fun getTodayExercises(): Int
}