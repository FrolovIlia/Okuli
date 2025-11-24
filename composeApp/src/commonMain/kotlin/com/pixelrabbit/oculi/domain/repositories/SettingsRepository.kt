package com.pixelrabbit.oculi.domain.repositories

interface SettingsRepository {
    suspend fun isDarkThemeEnabled(): Boolean
    suspend fun setDarkThemeEnabled(enabled: Boolean)
    suspend fun areNotificationsEnabled(): Boolean
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun getReminderInterval(): Int
    suspend fun setReminderInterval(interval: Int)
    suspend fun isReminderEnabled(): Boolean       // ← вот этот метод
    suspend fun setReminderEnabled(enabled: Boolean)
}


