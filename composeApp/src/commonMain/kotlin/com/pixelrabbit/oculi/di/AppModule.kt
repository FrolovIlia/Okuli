package com.pixelrabbit.oculi.di

import com.pixelrabbit.oculi.data.repositories.ExerciseRepositoryImpl
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import com.pixelrabbit.oculi.domain.use_cases.GetExercisesUseCase
import com.pixelrabbit.oculi.domain.use_cases.StartExerciseUseCase
import org.koin.dsl.module

val appModule = module {
    single<ExerciseRepository> { ExerciseRepositoryImpl() }

    factory { GetExercisesUseCase(get()) }
    factory { StartExerciseUseCase(get()) }
}