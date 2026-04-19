package com.pixelrabbit.backy.di

import com.pixelrabbit.backy.data.repositories.LastVisitRepository
import com.pixelrabbit.backy.data.repositories.LastVisitRepositoryImpl
import com.pixelrabbit.backy.data.repositories.RealAchievementRepository
import com.pixelrabbit.backy.data.repositories.RealStatsRepository
import com.pixelrabbit.backy.domain.repositories.*
import com.pixelrabbit.backy.domain.use_cases.*
import com.pixelrabbit.backy.notification.NotificationManager

expect fun createSettingsRepository(): SettingsRepository

object ServiceLocator {
    private var _notificationManager: NotificationManager? = null

    fun init(notificationManager: NotificationManager) {
        _notificationManager = notificationManager
    }

    fun getNotificationManager(): NotificationManager {
        return _notificationManager ?: error("ServiceLocator не инициализирован. Вызовите init()")
    }

    private val _exerciseRepository: ExerciseRepository by lazy { ExerciseRepositoryImpl() }
    private val _statsRepository: StatsRepository by lazy { RealStatsRepository() }
    private val _achievementRepository: AchievementRepository by lazy { RealAchievementRepository() }
    private val _settingsRepository: SettingsRepository by lazy { createSettingsRepository() }
    private val _lastVisitRepository: LastVisitRepository by lazy { LastVisitRepositoryImpl() }

    val getExercisesUseCase: GetExercisesUseCase by lazy { GetExercisesUseCase(_exerciseRepository) }
    val startExerciseUseCase: StartExerciseUseCase by lazy {
        StartExerciseUseCase(_exerciseRepository, _statsRepository)
    }
    val getStatsUseCase: GetStatsUseCase by lazy { GetStatsUseCase(_statsRepository) }
    val getAchievementsUseCase: GetAchievementsUseCase by lazy {
        GetAchievementsUseCase(_achievementRepository)
    }
    val getSettingsUseCase: GetSettingsUseCase by lazy { GetSettingsUseCase(_settingsRepository) }
    val updateSettingsUseCase: UpdateSettingsUseCase by lazy { UpdateSettingsUseCase(_settingsRepository) }
    val adUseCase: AdUseCase by lazy { AdUseCase(_settingsRepository) }
    val checkDailyVisitUseCase: CheckDailyVisitUseCase by lazy { CheckDailyVisitUseCase(_lastVisitRepository) }

    fun statsRepository(): StatsRepository = _statsRepository
    fun achievementRepository(): AchievementRepository = _achievementRepository
    fun exerciseRepository(): ExerciseRepository = _exerciseRepository
    fun settingsRepository(): SettingsRepository = _settingsRepository
    fun lastVisitRepository(): LastVisitRepository = _lastVisitRepository
}