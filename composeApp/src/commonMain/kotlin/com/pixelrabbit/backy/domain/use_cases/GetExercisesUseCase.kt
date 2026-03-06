package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.domain.models.Exercise
import com.pixelrabbit.backy.domain.repositories.ExerciseRepository

class GetExercisesUseCase(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(): List<Exercise> {
        return repository.getAllExercises()
    }

    suspend operator fun invoke(type: String): List<Exercise> {
        val exerciseType = when (type) {
            "ACCOMMODATION" -> com.pixelrabbit.backy.domain.models.ExerciseType.ACCOMMODATION
            "RELAXATION" -> com.pixelrabbit.backy.domain.models.ExerciseType.RELAXATION
            "MOBILITY" -> com.pixelrabbit.backy.domain.models.ExerciseType.MOBILITY
            else -> com.pixelrabbit.backy.domain.models.ExerciseType.ACCOMMODATION
        }
        return repository.getExercisesByType(exerciseType)
    }
}