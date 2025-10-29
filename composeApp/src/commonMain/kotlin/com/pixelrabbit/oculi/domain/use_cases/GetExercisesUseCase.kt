package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.models.Exercise
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository

class GetExercisesUseCase(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(): List<Exercise> {
        return repository.getAllExercises()
    }

    suspend operator fun invoke(type: String): List<Exercise> {
        val exerciseType = when (type) {
            "ACCOMMODATION" -> com.pixelrabbit.oculi.domain.models.ExerciseType.ACCOMMODATION
            "RELAXATION" -> com.pixelrabbit.oculi.domain.models.ExerciseType.RELAXATION
            "MOBILITY" -> com.pixelrabbit.oculi.domain.models.ExerciseType.MOBILITY
            else -> com.pixelrabbit.oculi.domain.models.ExerciseType.ACCOMMODATION
        }
        return repository.getExercisesByType(exerciseType)
    }
}