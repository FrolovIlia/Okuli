package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.repositories.StatsRepository

class StatsRepositoryImpl : StatsRepository {
    // Демонстрационные данные
    override suspend fun getTotalExercises(): Int = 12
    override suspend fun getTotalTime(): Int = 45 // в минутах
    override suspend fun getCurrentStreak(): Int = 3 // дней подряд
    override suspend fun getTodayExercises(): Int = 2
}