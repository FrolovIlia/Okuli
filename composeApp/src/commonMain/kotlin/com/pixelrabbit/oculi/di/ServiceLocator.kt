package com.pixelrabbit.oculi.di

import com.pixelrabbit.oculi.data.repositories.ExerciseRepositoryImpl
import com.pixelrabbit.oculi.data.repositories.StatsRepositoryImpl
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import com.pixelrabbit.oculi.domain.use_cases.GetExercisesUseCase
import com.pixelrabbit.oculi.domain.use_cases.StartExerciseUseCase

object ServiceLocator {
    private val exerciseRepository: ExerciseRepository by lazy { ExerciseRepositoryImpl() }
    private val statsRepository: StatsRepository by lazy { StatsRepositoryImpl() }

    val getExercisesUseCase: GetExercisesUseCase by lazy { GetExercisesUseCase(exerciseRepository) }
    val startExerciseUseCase: StartExerciseUseCase by lazy { StartExerciseUseCase(exerciseRepository) }
}