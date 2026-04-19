package com.pixelrabbit.backy.utils

class AppSettings {
    var isOnboardingCompleted: Boolean
        get() = PlatformPreferences.getBoolean(ONBOARDING_COMPLETED_KEY, false)
        set(value) = PlatformPreferences.setBoolean(ONBOARDING_COMPLETED_KEY, value)

    var launchCount: Int
        get() = PlatformPreferences.getInt(LAUNCH_COUNT_KEY, 0)
        set(value) = PlatformPreferences.setInt(LAUNCH_COUNT_KEY, value)

    var shouldShowAds: Boolean
        get() = PlatformPreferences.getBoolean(SHOULD_SHOW_ADS_KEY, false)
        set(value) = PlatformPreferences.setBoolean(SHOULD_SHOW_ADS_KEY, value)

    var adsRemoved: Boolean
        get() = PlatformPreferences.getBoolean(ADS_REMOVED_KEY, false)
        set(value) = PlatformPreferences.setBoolean(ADS_REMOVED_KEY, value)

    var transactionId: String?
        get() = PlatformPreferences.getString(TRANSACTION_ID_KEY, null)
        set(value) = PlatformPreferences.setString(TRANSACTION_ID_KEY, value)

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
        private const val ADS_REMOVED_KEY = "ads_removed"
        private const val TRANSACTION_ID_KEY = "transaction_id"
        private const val DARK_THEME_KEY = "dark_theme_enabled"
        private const val NOTIFICATIONS_KEY = "notifications_enabled"
        private const val REMINDER_ENABLED_KEY = "reminder_enabled"
        private const val REMINDER_INTERVAL_KEY = "reminder_interval"
    }
}