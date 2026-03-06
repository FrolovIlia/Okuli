package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.domain.repositories.NotificationRepository

class ManageNotificationsUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(
        enabled: Boolean,
        intervalHours: Int = 24
    ) {
        repository.setNotificationEnabled(enabled)

        if (enabled) {
            repository.scheduleReminder(intervalHours)
        } else {
            repository.cancelReminders()
        }
    }
}