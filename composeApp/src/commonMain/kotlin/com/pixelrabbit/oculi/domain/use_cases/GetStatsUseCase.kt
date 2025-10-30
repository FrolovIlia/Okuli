package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.repositories.StatsRepository

class GetStatsUseCase(
    private val repository: StatsRepository
) {
    suspend operator fun invoke(): StatsResult {
        return StatsResult(
            totalExercises = repository.getTotalExercises(),
            totalTime = repository.getTotalTime(),
            currentStreak = repository.getCurrentStreak(),
            todayExercises = repository.getTodayExercises()
        )
    }
}

data class StatsResult(
    val totalExercises: Int,
    val totalTime: Int, // в минутах
    val currentStreak: Int,
    val todayExercises: Int
)