package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.repositories.SettingsRepository

class SettingsRepositoryImpl : SettingsRepository {
    private var darkThemeEnabled: Boolean = false
    private var notificationsEnabled: Boolean = true
    private var reminderInterval: Int = 60

    override suspend fun isDarkThemeEnabled(): Boolean = darkThemeEnabled

    override suspend fun setDarkThemeEnabled(enabled: Boolean) {
        darkThemeEnabled = enabled
        println("Темная тема: ${if (enabled) "включена" else "выключена"}")
    }

    override suspend fun areNotificationsEnabled(): Boolean = notificationsEnabled

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        notificationsEnabled = enabled
        println("Уведомления: ${if (enabled) "включены" else "выключены"}")
    }

    override suspend fun getReminderInterval(): Int = reminderInterval

    override suspend fun setReminderInterval(interval: Int) {
        reminderInterval = interval
        println("Интервал напоминаний: $interval минут")
    }
}