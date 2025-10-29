package com.pixelrabbit.oculi.domain.repositories

import com.pixelrabbit.oculi.domain.models.Exercise
import com.pixelrabbit.oculi.domain.models.ExerciseType

interface ExerciseRepository {
    suspend fun getAllExercises(): List<Exercise>
    suspend fun getExerciseById(id: String): Exercise?
    suspend fun getExercisesByType(type: ExerciseType): List<Exercise>
    suspend fun markExerciseCompleted(exerciseId: String, duration: Int)
}