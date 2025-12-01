package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.repositories.SettingsRepository
import com.pixelrabbit.oculi.utils.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl : SettingsRepository {
    private val appSettings = AppSettings()

    override suspend fun isDarkThemeEnabled(): Boolean {
        return withContext(Dispatchers.Default) {
            appSettings.darkThemeEnabled
        }
    }

    override suspend fun setDarkThemeEnabled(enabled: Boolean) {
        withContext(Dispatchers.Default) {
            appSettings.darkThemeEnabled = enabled
            println("Темная тема сохранена: ${if (enabled) "включена" else "выключена"}")
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
            println("Уведомления сохранены: ${if (enabled) "включены" else "выключены"}")
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
            println("Напоминания сохранены: ${if (enabled) "включены" else "выключены"}")
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
            println("Интервал напоминаний сохранен: $interval минут")
        }
    }

    // Опционально: метод для получения всех настроек сразу
    suspend fun getAllSettings(): Map<String, Any> {
        return withContext(Dispatchers.Default) {
            mapOf(
                "darkThemeEnabled" to appSettings.darkThemeEnabled,
                "notificationsEnabled" to appSettings.notificationsEnabled,
                "reminderEnabled" to appSettings.reminderEnabled,
                "reminderInterval" to appSettings.reminderInterval,
                "isOnboardingCompleted" to appSettings.isOnboardingCompleted
            )
        }
    }
}