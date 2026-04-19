package com.pixelrabbit.backy.data.repositories

import android.util.Log
import com.pixelrabbit.backy.domain.repositories.SettingsRepository
import com.pixelrabbit.backy.utils.AppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl : SettingsRepository {
    private val appSettings = AppSettings()
    private val TAG = "PAYMENT_DEBUG"

    override suspend fun incrementLaunchCount(): Boolean {
        return withContext(Dispatchers.Default) {
            val currentCount = appSettings.launchCount + 1
            appSettings.launchCount = currentCount

            val shouldShowAds = currentCount >= 3 && !appSettings.adsRemoved
            if (shouldShowAds && !appSettings.shouldShowAds) {
                appSettings.shouldShowAds = true
            }

            Log.d(TAG, "incrementLaunchCount: count=$currentCount, shouldShowAds=$shouldShowAds, adsRemoved=${appSettings.adsRemoved}")
            shouldShowAds
        }
    }

    override suspend fun shouldShowAds(): Boolean {
        return withContext(Dispatchers.Default) {
            val result = if (appSettings.adsRemoved) false else appSettings.shouldShowAds
            Log.d(TAG, "shouldShowAds: $result (adsRemoved=${appSettings.adsRemoved}, shouldShowAds=${appSettings.shouldShowAds})")
            result
        }
    }

    override suspend fun getLaunchCount(): Int {
        return withContext(Dispatchers.Default) { appSettings.launchCount }
    }

    override suspend fun setAdsRemoved(removed: Boolean) {
        withContext(Dispatchers.Default) {
            Log.d(TAG, "setAdsRemoved called with: $removed")
            appSettings.adsRemoved = removed
            if (removed) {
                appSettings.shouldShowAds = false
                Log.d(TAG, "setAdsRemoved: shouldShowAds set to false")
            }
        }
    }

    override suspend fun isAdsRemoved(): Boolean {
        return withContext(Dispatchers.Default) {
            val result = appSettings.adsRemoved
            Log.d(TAG, "isAdsRemoved returning: $result")
            result
        }
    }

    override suspend fun setTransactionId(transactionId: String) {
        withContext(Dispatchers.Default) {
            Log.d(TAG, "setTransactionId: $transactionId")
            appSettings.transactionId = transactionId
        }
    }

    override suspend fun getTransactionId(): String? {
        return withContext(Dispatchers.Default) {
            val result = appSettings.transactionId
            Log.d(TAG, "getTransactionId returning: $result")
            result
        }
    }

    override suspend fun isDarkThemeEnabled(): Boolean {
        return withContext(Dispatchers.Default) { appSettings.darkThemeEnabled }
    }

    override suspend fun setDarkThemeEnabled(enabled: Boolean) {
        withContext(Dispatchers.Default) { appSettings.darkThemeEnabled = enabled }
    }

    override suspend fun areNotificationsEnabled(): Boolean {
        return withContext(Dispatchers.Default) { appSettings.notificationsEnabled }
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        withContext(Dispatchers.Default) { appSettings.notificationsEnabled = enabled }
    }

    override suspend fun isReminderEnabled(): Boolean {
        return withContext(Dispatchers.Default) { appSettings.reminderEnabled }
    }

    override suspend fun setReminderEnabled(enabled: Boolean) {
        withContext(Dispatchers.Default) { appSettings.reminderEnabled = enabled }
    }

    override suspend fun getReminderInterval(): Int {
        return withContext(Dispatchers.Default) { appSettings.reminderInterval }
    }

    override suspend fun setReminderInterval(interval: Int) {
        withContext(Dispatchers.Default) { appSettings.reminderInterval = interval }
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
                "shouldShowAds" to appSettings.shouldShowAds,
                "adsRemoved" to appSettings.adsRemoved
            )
        }
    }
}