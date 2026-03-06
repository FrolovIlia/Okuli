package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.domain.models.UserProgress
import com.pixelrabbit.backy.domain.repositories.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class GetStatsUseCase(private val repository: StatsRepository) {
    suspend operator fun invoke(): UserProgress {
        val progress = repository.getUserProgress()
        return checkAndFixDailyStats(progress)
    }

    fun getStatsFlow(): Flow<UserProgress> = repository.getUserProgressFlow()
        .map { progress -> checkAndFixDailyStats(progress) }

    private fun checkAndFixDailyStats(progress: UserProgress): UserProgress {
        val now = Clock.System.now().toEpochMilliseconds()
        if (progress.lastActivityDate == 0L) return progress

        val lastDay = progress.lastActivityDate / (24 * 60 * 60 * 1000) // Дни с эпохи
        val currentDay = now / (24 * 60 * 60 * 1000)

        if (lastDay != currentDay) {
            val newStreak = if (currentDay - lastDay == 1L) progress.currentStreak + 1 else 1

            return progress.copy(
                todayExercises = 0,
                currentStreak = newStreak
            )
        }

        return progress
    }

    private fun timestampToDate(timestamp: Long): LocalDate {
        return timestamp.toLocalDateTime().date
    }

    private fun Long.toLocalDateTime(): kotlinx.datetime.LocalDateTime {
        return kotlinx.datetime.Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }
}