// shared/src/commonMain/kotlin/com/pixelrabbit/oculi/utils/SettingsManager.kt
package com.pixelrabbit.oculi.utils

object SettingsManager {
    private val appSettings = AppSettings()

    // Тема
    fun isDarkThemeEnabled(): Boolean = appSettings.darkThemeEnabled
    fun setDarkThemeEnabled(enabled: Boolean) {
        appSettings.darkThemeEnabled = enabled
    }

    // Уведомления
    fun areNotificationsEnabled(): Boolean = appSettings.notificationsEnabled
    fun setNotificationsEnabled(enabled: Boolean) {
        appSettings.notificationsEnabled = enabled
    }

    // Напоминания
    fun isReminderEnabled(): Boolean = appSettings.reminderEnabled
    fun setReminderEnabled(enabled: Boolean) {
        appSettings.reminderEnabled = enabled
    }

    fun getReminderInterval(): Int = appSettings.reminderInterval
    fun setReminderInterval(interval: Int) {
        appSettings.reminderInterval = interval
    }

    // Onboarding
    fun isOnboardingCompleted(): Boolean = appSettings.isOnboardingCompleted
    fun setOnboardingCompleted(completed: Boolean) {
        appSettings.isOnboardingCompleted = completed
    }

    // Получить все настройки
    fun getAllSettings(): SettingsData {
        return SettingsData(
            darkThemeEnabled = isDarkThemeEnabled(),
            notificationsEnabled = areNotificationsEnabled(),
            reminderEnabled = isReminderEnabled(),
            reminderInterval = getReminderInterval(),
            onboardingCompleted = isOnboardingCompleted()
        )
    }
}

data class SettingsData(
    val darkThemeEnabled: Boolean,
    val notificationsEnabled: Boolean,
    val reminderEnabled: Boolean,
    val reminderInterval: Int,
    val onboardingCompleted: Boolean
)