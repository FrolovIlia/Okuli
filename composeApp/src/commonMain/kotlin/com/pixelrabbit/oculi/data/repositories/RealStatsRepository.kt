package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RealStatsRepository : StatsRepository {
    private val _stats = MutableStateFlow(StatsData())

    override suspend fun getTotalExercises(): Int = _stats.value.totalExercises
    override suspend fun getTotalTime(): Int = _stats.value.totalTime
    override suspend fun getCurrentStreak(): Int = _stats.value.currentStreak
    override suspend fun getTodayExercises(): Int = _stats.value.todayExercises

    suspend fun incrementExercises(duration: Int) {
        _stats.value = _stats.value.copy(
            totalExercises = _stats.value.totalExercises + 1,
            totalTime = _stats.value.totalTime + (duration / 60),
            todayExercises = _stats.value.todayExercises + 1
        )
    }

    private data class StatsData(
        val totalExercises: Int = 0,
        val totalTime: Int = 0,
        val currentStreak: Int = 0,
        val todayExercises: Int = 0
    )
}