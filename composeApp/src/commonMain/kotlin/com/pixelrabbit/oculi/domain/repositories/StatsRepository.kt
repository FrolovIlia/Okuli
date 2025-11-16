package com.pixelrabbit.oculi.domain.repositories

import com.pixelrabbit.oculi.domain.models.UserProgress
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    suspend fun getUserProgress(): UserProgress
    fun getUserProgressFlow(): Flow<UserProgress>
    suspend fun addExerciseCompletion(
        exerciseId: String,
        exerciseName: String,
        duration: Int,
        difficulty: String,
        successRate: Float
    )
    suspend fun resetDailyStats()
}