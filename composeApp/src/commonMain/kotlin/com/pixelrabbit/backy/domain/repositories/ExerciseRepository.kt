package com.pixelrabbit.backy.domain.repositories

import com.pixelrabbit.backy.domain.models.Exercise
import com.pixelrabbit.backy.domain.models.ExerciseType

interface ExerciseRepository {
    suspend fun getAllExercises(): List<Exercise>
    suspend fun getExerciseById(id: String): Exercise?
    suspend fun getExercisesByType(type: ExerciseType): List<Exercise>
    suspend fun getFreeExercises(): List<Exercise>
    suspend fun markExerciseCompleted(exerciseId: String, duration: Int)
}