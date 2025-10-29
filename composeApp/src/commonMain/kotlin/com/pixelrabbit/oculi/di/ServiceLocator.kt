package com.pixelrabbit.oculi.di

import com.pixelrabbit.oculi.data.repositories.ExerciseRepositoryImpl
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import com.pixelrabbit.oculi.domain.use_cases.GetExercisesUseCase
import com.pixelrabbit.oculi.domain.use_cases.StartExerciseUseCase

object ServiceLocator {
    private val exerciseRepository: ExerciseRepository by lazy { ExerciseRepositoryImpl() }

    val getExercisesUseCase: GetExercisesUseCase by lazy { GetExercisesUseCase(exerciseRepository) }
    val startExerciseUseCase: StartExerciseUseCase by lazy { StartExerciseUseCase(exerciseRepository) }
}