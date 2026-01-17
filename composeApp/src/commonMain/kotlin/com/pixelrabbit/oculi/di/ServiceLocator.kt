// shared/src/commonMain/kotlin/com/pixelrabbit/oculi/di/ServiceLocator.kt
package com.pixelrabbit.oculi.di

import com.pixelrabbit.oculi.data.repositories.*
import com.pixelrabbit.oculi.domain.repositories.*
import com.pixelrabbit.oculi.domain.use_cases.*
import com.pixelrabbit.oculi.notification.NotificationManager

object ServiceLocator {
    // Платформенные зависимости
    private var _notificationManager: NotificationManager? = null

    fun init(notificationManager: NotificationManager) {
        _notificationManager = notificationManager
    }

    fun getNotificationManager(): NotificationManager {
        return _notificationManager ?: error("ServiceLocator не инициализирован. Вызовите init()")
    }

    // Приватные репозитории
    private val _exerciseRepository: ExerciseRepository by lazy { ExerciseRepositoryImpl() }
    private val _statsRepository: StatsRepository by lazy { RealStatsRepository() }
    private val _achievementRepository: AchievementRepository by lazy { RealAchievementRepository() }
    private val _settingsRepository: SettingsRepository by lazy { SettingsRepositoryImpl() }
    private val _lastVisitRepository: LastVisitRepository by lazy { LastVisitRepositoryImpl() }

    // Публичные Use Cases
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

    // Методы доступа для исправления ошибок компиляции
    fun statsRepository(): StatsRepository = _statsRepository
    fun achievementRepository(): AchievementRepository = _achievementRepository
    fun exerciseRepository(): ExerciseRepository = _exerciseRepository
    fun settingsRepository(): SettingsRepository = _settingsRepository
    fun lastVisitRepository(): LastVisitRepository = _lastVisitRepository
}