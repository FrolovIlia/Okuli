package com.pixelrabbit.oculi.di

import com.pixelrabbit.oculi.data.repositories.*
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepositoryImpl
import com.pixelrabbit.oculi.domain.repositories.AchievementRepository
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import com.pixelrabbit.oculi.domain.repositories.NotificationRepository
import com.pixelrabbit.oculi.domain.repositories.SettingsRepository
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import com.pixelrabbit.oculi.domain.use_cases.*
import com.pixelrabbit.oculi.domain.use_cases.CheckDailyVisitUseCase

object ServiceLocator {
    // Приватные репозитории
    private val _exerciseRepository: ExerciseRepository by lazy { ExerciseRepositoryImpl() }
    private val _statsRepository: StatsRepository by lazy { RealStatsRepository() }
    private val _notificationRepository: NotificationRepository by lazy { NotificationRepositoryImpl() }
    private val _achievementRepository: AchievementRepository by lazy { RealAchievementRepository() }
    private val _settingsRepository: SettingsRepository by lazy { SettingsRepositoryImpl() }
    private val _lastVisitRepository: LastVisitRepository by lazy { LastVisitRepositoryImpl() }

    // Публичные Use Cases
    val getExercisesUseCase: GetExercisesUseCase by lazy { GetExercisesUseCase(_exerciseRepository) }
    val startExerciseUseCase: StartExerciseUseCase by lazy {
        StartExerciseUseCase(_exerciseRepository, _statsRepository)
    }
    val getStatsUseCase: GetStatsUseCase by lazy { GetStatsUseCase(_statsRepository) }
    val manageNotificationsUseCase: ManageNotificationsUseCase by lazy {
        ManageNotificationsUseCase(_notificationRepository)
    }
    val getAchievementsUseCase: GetAchievementsUseCase by lazy {
        GetAchievementsUseCase(_achievementRepository)
    }
    val getSettingsUseCase: GetSettingsUseCase by lazy { GetSettingsUseCase(_settingsRepository) }
    val updateSettingsUseCase: UpdateSettingsUseCase by lazy { UpdateSettingsUseCase(_settingsRepository) }
    val adUseCase: AdUseCase by lazy { AdUseCase(_settingsRepository) }
    val checkDailyVisitUseCase: CheckDailyVisitUseCase by lazy { CheckDailyVisitUseCase(_lastVisitRepository) }

    // Прямой доступ к репозиториям
    fun statsRepository(): StatsRepository = _statsRepository
    fun achievementRepository(): AchievementRepository = _achievementRepository
    fun exerciseRepository(): ExerciseRepository = _exerciseRepository
    fun notificationRepository(): NotificationRepository = _notificationRepository
    fun settingsRepository(): SettingsRepository = _settingsRepository
    fun lastVisitRepository(): LastVisitRepository = _lastVisitRepository
}