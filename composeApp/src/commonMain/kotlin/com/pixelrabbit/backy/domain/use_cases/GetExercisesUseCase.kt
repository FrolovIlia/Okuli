package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.domain.models.Exercise
import com.pixelrabbit.backy.domain.models.ExerciseType
import com.pixelrabbit.backy.domain.repositories.ExerciseRepository

class GetExercisesUseCase(
    private val repository: ExerciseRepository
) {

    suspend operator fun invoke(): List<Exercise> {
        return repository.getAllExercises()
    }

    suspend operator fun invoke(type: String): List<Exercise> {

        val exerciseType = try {
            ExerciseType.valueOf(type)
        } catch (e: IllegalArgumentException) {
            ExerciseType.MOBILITY
        }

        return repository.getExercisesByType(exerciseType)
    }
}