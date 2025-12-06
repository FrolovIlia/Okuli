package com.pixelrabbit.oculi.domain.repositories

interface NotificationRepository {
    suspend fun scheduleReminder(intervalHours: Int)
    suspend fun cancelReminders()
    suspend fun isNotificationEnabled(): Boolean
    suspend fun setNotificationEnabled(enabled: Boolean)
}