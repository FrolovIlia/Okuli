package com.pixelrabbit.backy.utils

class AppSettings {
    // Существующие настройки
    var isOnboardingCompleted: Boolean
        get() = PlatformPreferences.getBoolean(ONBOARDING_COMPLETED_KEY, false)
        set(value) = PlatformPreferences.setBoolean(ONBOARDING_COMPLETED_KEY, value)

    // Новые настройки для рекламы
    var launchCount: Int
        get() = PlatformPreferences.getInt(LAUNCH_COUNT_KEY, 0)
        set(value) = PlatformPreferences.setInt(LAUNCH_COUNT_KEY, value)

    var shouldShowAds: Boolean
        get() = PlatformPreferences.getBoolean(SHOULD_SHOW_ADS_KEY, false)
        set(value) = PlatformPreferences.setBoolean(SHOULD_SHOW_ADS_KEY, value)

    // Существующие настройки темы и уведомлений
    var darkThemeEnabled: Boolean
        get() = PlatformPreferences.getBoolean(DARK_THEME_KEY, false)
        set(value) = PlatformPreferences.setBoolean(DARK_THEME_KEY, value)

    var notificationsEnabled: Boolean
        get() = PlatformPreferences.getBoolean(NOTIFICATIONS_KEY, true)
        set(value) = PlatformPreferences.setBoolean(NOTIFICATIONS_KEY, value)

    var reminderEnabled: Boolean
        get() = PlatformPreferences.getBoolean(REMINDER_ENABLED_KEY, true)
        set(value) = PlatformPreferences.setBoolean(REMINDER_ENABLED_KEY, value)

    var reminderInterval: Int
        get() = PlatformPreferences.getInt(REMINDER_INTERVAL_KEY, 24)
        set(value) = PlatformPreferences.setInt(REMINDER_INTERVAL_KEY, value)

    companion object {
        private const val ONBOARDING_COMPLETED_KEY = "onboarding_completed"
        private const val LAUNCH_COUNT_KEY = "launch_count"
        private const val SHOULD_SHOW_ADS_KEY = "should_show_ads"
        private const val DARK_THEME_KEY = "dark_theme_enabled"
        private const val NOTIFICATIONS_KEY = "notifications_enabled"
        private const val REMINDER_ENABLED_KEY = "reminder_enabled"
        private const val REMINDER_INTERVAL_KEY = "reminder_interval"
    }
}