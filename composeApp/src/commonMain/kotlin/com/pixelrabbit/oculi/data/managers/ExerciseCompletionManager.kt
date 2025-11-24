package com.pixelrabbit.oculi.data.managers

import com.pixelrabbit.oculi.core.AppIODispatcher
import com.pixelrabbit.oculi.di.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object ExerciseCompletionManager {

    private val scope = CoroutineScope(AppIODispatcher + Job())
    private val pendingCompletions = mutableSetOf<String>()

    fun saveExerciseCompletion(
        exerciseId: String,
        exerciseName: String,
        duration: Int,
        difficulty: String,
        successRate: Float = 100f
    ) {
        if (pendingCompletions.contains(exerciseId)) {
            println("DEBUG: Exercise $exerciseId already being saved, skipping")
            return
        }

        pendingCompletions.add(exerciseId)

        scope.launch {
            try {
                println("DEBUG: Starting background save for $exerciseId")

                ServiceLocator.startExerciseUseCase(
                    exerciseId = exerciseId,
                    exerciseName = exerciseName,
                    actualDuration = duration,
                    difficulty = difficulty,
                    successRate = successRate
                )

                println("DEBUG: Background save completed for $exerciseId")
            } catch (e: Exception) {
                println("DEBUG: Error in background save for $exerciseId: ${e.message}")
                e.printStackTrace()
            } finally {
                pendingCompletions.remove(exerciseId)
            }
        }
    }
}
