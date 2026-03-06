package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.domain.repositories.ExerciseRepository
import com.pixelrabbit.backy.domain.repositories.StatsRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class StartExerciseUseCase(
    private val exerciseRepository: ExerciseRepository,
    private val statsRepository: StatsRepository
) {
    suspend operator fun invoke(
        exerciseId: String,
        exerciseName: String,
        actualDuration: Int,
        difficulty: String,
        successRate: Float = 100f
    ) {
        val progress = statsRepository.getUserProgress()
        val now = Clock.System.now().toEpochMilliseconds()

        if (progress.lastActivityDate > 0) {
            val lastDate = progress.lastActivityDate.toLocalDateTime().date
            val currentDate = now.toLocalDateTime().date

            // Вычисляем вчерашнюю дату
            val yesterday = LocalDate(
                year = currentDate.year,
                month = currentDate.month,
                dayOfMonth = currentDate.dayOfMonth - 1
            )

            if (lastDate != currentDate && lastDate != yesterday) {
                statsRepository.resetDailyStats()
            }
        }

        statsRepository.addExerciseCompletion(
            exerciseId = exerciseId,
            exerciseName = exerciseName,
            duration = actualDuration,
            difficulty = difficulty,
            successRate = successRate
        )

        exerciseRepository.markExerciseCompleted(exerciseId, actualDuration)
    }

    private fun Long.toLocalDateTime(): kotlinx.datetime.LocalDateTime {
        return kotlinx.datetime.Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }
}