package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.repositories.NotificationRepository

class ManageNotificationsUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(enabled: Boolean, intervalMinutes: Int = 60) {
        repository.setNotificationEnabled(enabled)

        if (enabled) {
            repository.scheduleReminder(intervalMinutes)
        } else {
            repository.cancelReminders()
        }
    }
}