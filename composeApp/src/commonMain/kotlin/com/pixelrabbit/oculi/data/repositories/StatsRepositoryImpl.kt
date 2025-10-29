package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.repositories.StatsRepository

class StatsRepositoryImpl : StatsRepository {
    override suspend fun getTotalExercises(): Int = 0
    override suspend fun getTotalTime(): Int = 0
    override suspend fun getCurrentStreak(): Int = 0
    override suspend fun getTodayExercises(): Int = 0
}