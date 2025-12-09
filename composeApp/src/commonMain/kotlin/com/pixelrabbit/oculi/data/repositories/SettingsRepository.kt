package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.repositories.SettingsRepository
import com.pixelrabbit.oculi.utils.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl : SettingsRepository {
    private val appSettings = AppSettings()

    // === Методы для рекламы ===
    override suspend fun incrementLaunchCount(): Boolean {
        return withContext(Dispatchers.Default) {
            val currentCount = appSettings.launchCount + 1
            appSettings.launchCount = currentCount

            val shouldShowAds = currentCount >= 3
            if (shouldShowAds && !appSettings.shouldShowAds) {
                appSettings.shouldShowAds = true
            }

            shouldShowAds
        }
    }

    override suspend fun shouldShowAds(): Boolean {
        return withContext(Dispatchers.Default) {
            appSettings.shouldShowAds
        }
    }

    override suspend fun getLaunchCount(): Int {
        return withContext(Dispatchers.Default) {
            appSettings.launchCount
        }
    }

    // === Существующие методы ===
    override suspend fun isDarkThemeEnabled(): Boolean {
        return withContext(Dispatchers.Default) {
            appSettings.darkThemeEnabled
        }
    }

    override suspend fun setDarkThemeEnabled(enabled: Boolean) {
        withContext(Dispatchers.Default) {
            appSettings.darkThemeEnabled = enabled
        }
    }

    override suspend fun areNotificationsEnabled(): Boolean {
        return withContext(Dispatchers.Default) {
            appSettings.notificationsEnabled
        }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        withContext(Dispatchers.Default) {
            appSettings.notificationsEnabled = enabled
        }
    }

    override suspend fun isReminderEnabled(): Boolean {
        return withContext(Dispatchers.Default) {
            appSettings.reminderEnabled
        }
    }

    override suspend fun setReminderEnabled(enabled: Boolean) {
        withContext(Dispatchers.Default) {
            appSettings.reminderEnabled = enabled
        }
    }

    override suspend fun getReminderInterval(): Int {
        return withContext(Dispatchers.Default) {
            appSettings.reminderInterval
        }
    }

    override suspend fun setReminderInterval(interval: Int) {
        withContext(Dispatchers.Default) {
            appSettings.reminderInterval = interval
        }
    }

    override suspend fun getAllSettings(): Map<String, Any> {
        return withContext(Dispatchers.Default) {
            mapOf(
                "darkThemeEnabled" to appSettings.darkThemeEnabled,
                "notificationsEnabled" to appSettings.notificationsEnabled,
                "reminderEnabled" to appSettings.reminderEnabled,
                "reminderInterval" to appSettings.reminderInterval,
                "isOnboardingCompleted" to appSettings.isOnboardingCompleted,
                "launchCount" to appSettings.launchCount,
                "shouldShowAds" to appSettings.shouldShowAds
            )
        }
    }
}