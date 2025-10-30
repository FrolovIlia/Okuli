package com.pixelrabbit.oculi.di

import com.pixelrabbit.oculi.data.repositories.AchievementRepositoryImpl
import com.pixelrabbit.oculi.data.repositories.ExerciseRepositoryImpl
import com.pixelrabbit.oculi.data.repositories.NotificationRepositoryImpl
import com.pixelrabbit.oculi.data.repositories.StatsRepositoryImpl
import com.pixelrabbit.oculi.domain.repositories.AchievementRepository
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import com.pixelrabbit.oculi.domain.repositories.NotificationRepository
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import com.pixelrabbit.oculi.domain.use_cases.GetAchievementsUseCase
import com.pixelrabbit.oculi.domain.use_cases.GetExercisesUseCase
import com.pixelrabbit.oculi.domain.use_cases.GetStatsUseCase
import com.pixelrabbit.oculi.domain.use_cases.ManageNotificationsUseCase
import com.pixelrabbit.oculi.domain.use_cases.StartExerciseUseCase

object ServiceLocator {
    private val exerciseRepository: ExerciseRepository by lazy { ExerciseRepositoryImpl() }
    private val statsRepository: StatsRepository by lazy { StatsRepositoryImpl() }
    private val notificationRepository: NotificationRepository by lazy { NotificationRepositoryImpl() }
    private val achievementRepository: AchievementRepository by lazy { AchievementRepositoryImpl() }

    val getExercisesUseCase: GetExercisesUseCase by lazy { GetExercisesUseCase(exerciseRepository) }
    val startExerciseUseCase: StartExerciseUseCase by lazy { StartExerciseUseCase(exerciseRepository) }
    val getStatsUseCase: GetStatsUseCase by lazy { GetStatsUseCase(statsRepository) }
    val manageNotificationsUseCase: ManageNotificationsUseCase by lazy {
        ManageNotificationsUseCase(notificationRepository)
    }
    val getAchievementsUseCase: GetAchievementsUseCase by lazy {
        GetAchievementsUseCase(achievementRepository)
    }
}