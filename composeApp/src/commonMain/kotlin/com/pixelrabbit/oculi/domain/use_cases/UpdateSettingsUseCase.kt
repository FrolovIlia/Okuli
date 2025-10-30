package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.repositories.SettingsRepository

class UpdateSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(
        darkThemeEnabled: Boolean? = null,
        notificationsEnabled: Boolean? = null,
        reminderInterval: Int? = null
    ) {
        darkThemeEnabled?.let { repository.setDarkThemeEnabled(it) }
        notificationsEnabled?.let { repository.setNotificationsEnabled(it) }
        reminderInterval?.let { repository.setReminderInterval(it) }
    }
}