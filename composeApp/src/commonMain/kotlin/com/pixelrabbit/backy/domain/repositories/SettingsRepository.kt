package com.pixelrabbit.backy.domain.repositories

interface SettingsRepository {
    suspend fun incrementLaunchCount(): Boolean
    suspend fun shouldShowAds(): Boolean
    suspend fun getLaunchCount(): Int
    suspend fun setAdsRemoved(removed: Boolean)
    suspend fun isAdsRemoved(): Boolean
    suspend fun setTransactionId(transactionId: String)
    suspend fun getTransactionId(): String?
    suspend fun isDarkThemeEnabled(): Boolean
    suspend fun setDarkThemeEnabled(enabled: Boolean)
    suspend fun areNotificationsEnabled(): Boolean
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun isReminderEnabled(): Boolean
    suspend fun setReminderEnabled(enabled: Boolean)
    suspend fun getReminderInterval(): Int
    suspend fun setReminderInterval(interval: Int)
    suspend fun getAllSettings(): Map<String, Any>
}