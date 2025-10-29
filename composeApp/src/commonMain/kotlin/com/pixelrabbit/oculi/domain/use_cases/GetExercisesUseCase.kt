package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.models.Exercise
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import com.pixelrabbit.oculi.domain.repositories.UserRepository

class GetExercisesUseCase(
    private val repository: ExerciseRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<Exercise> {
        val isPremium = userRepository.isPremiumUser()
        return repository.getAllExercises()
            .filter { !it.isPremium || isPremium }
    }
}