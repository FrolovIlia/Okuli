package com.pixelrabbit.oculi.domain.repositories

interface SettingsRepository {
    // Методы для рекламы
    suspend fun incrementLaunchCount(): Boolean
    suspend fun shouldShowAds(): Boolean
    suspend fun getLaunchCount(): Int

    // Методы для темы
    suspend fun isDarkThemeEnabled(): Boolean
    suspend fun setDarkThemeEnabled(enabled: Boolean)

    // Методы для уведомлений
    suspend fun areNotificationsEnabled(): Boolean
    suspend fun setNotificationsEnabled(enabled: Boolean)

    // Методы для напоминаний
    suspend fun isReminderEnabled(): Boolean
    suspend fun setReminderEnabled(enabled: Boolean)
    suspend fun getReminderInterval(): Int
    suspend fun setReminderInterval(interval: Int)

    // Метод для получения всех настроек
    suspend fun getAllSettings(): Map<String, Any>
}