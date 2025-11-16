package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import com.pixelrabbit.oculi.domain.repositories.StatsRepository

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
        // Сохраняем в статистику
        statsRepository.addExerciseCompletion(
            exerciseId = exerciseId,
            exerciseName = exerciseName,
            duration = actualDuration,
            difficulty = difficulty,
            successRate = successRate
        )

        // Также отмечаем в репозитории упражнений если нужно
        exerciseRepository.markExerciseCompleted(exerciseId, actualDuration)
    }
}