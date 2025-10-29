package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository

class StartExerciseUseCase(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: String, actualDuration: Int) {
        repository.markExerciseCompleted(exerciseId, actualDuration)
    }
}