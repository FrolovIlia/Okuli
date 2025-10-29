package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository

class StartExerciseUseCase(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: String, actualDuration: Int) {
        exerciseRepository.markExerciseCompleted(exerciseId, actualDuration)
    }
}