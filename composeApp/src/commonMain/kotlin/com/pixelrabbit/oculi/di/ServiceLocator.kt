package com.pixelrabbit.oculi.di

import com.pixelrabbit.oculi.data.repositories.RealAchievementRepository
import com.pixelrabbit.oculi.data.repositories.RealStatsRepository
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepositoryImpl
import com.pixelrabbit.oculi.data.repositories.NotificationRepositoryImpl
import com.pixelrabbit.oculi.data.repositories.SettingsRepositoryImpl
import com.pixelrabbit.oculi.domain.repositories.AchievementRepository
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import com.pixelrabbit.oculi.domain.repositories.NotificationRepository
import com.pixelrabbit.oculi.domain.repositories.SettingsRepository
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import com.pixelrabbit.oculi.domain.use_cases.GetAchievementsUseCase
import com.pixelrabbit.oculi.domain.use_cases.GetExercisesUseCase
import com.pixelrabbit.oculi.domain.use_cases.GetSettingsUseCase
import com.pixelrabbit.oculi.domain.use_cases.GetStatsUseCase
import com.pixelrabbit.oculi.domain.use_cases.ManageNotificationsUseCase
import com.pixelrabbit.oculi.domain.use_cases.StartExerciseUseCase
import com.pixelrabbit.oculi.domain.use_cases.UpdateSettingsUseCase

object ServiceLocator {
    private val exerciseRepository: ExerciseRepository by lazy { ExerciseRepositoryImpl() }
    private val statsRepository: StatsRepository by lazy { RealStatsRepository() }
    private val notificationRepository: NotificationRepository by lazy { NotificationRepositoryImpl() }
    private val achievementRepository: AchievementRepository by lazy { RealAchievementRepository() }
    private val settingsRepository: SettingsRepository by lazy { SettingsRepositoryImpl() }

    val getExercisesUseCase: GetExercisesUseCase by lazy { GetExercisesUseCase(exerciseRepository) }
    val startExerciseUseCase: StartExerciseUseCase by lazy { StartExerciseUseCase(exerciseRepository) }
    val getStatsUseCase: GetStatsUseCase by lazy { GetStatsUseCase(statsRepository) }
    val manageNotificationsUseCase: ManageNotificationsUseCase by lazy {
        ManageNotificationsUseCase(notificationRepository)
    }
    val getAchievementsUseCase: GetAchievementsUseCase by lazy {
        GetAchievementsUseCase(achievementRepository)
    }
    val getSettingsUseCase: GetSettingsUseCase by lazy { GetSettingsUseCase(settingsRepository) }
    val updateSettingsUseCase: UpdateSettingsUseCase by lazy { UpdateSettingsUseCase(settingsRepository) }
}