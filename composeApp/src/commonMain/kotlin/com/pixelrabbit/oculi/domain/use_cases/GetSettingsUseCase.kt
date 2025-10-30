package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.repositories.SettingsRepository

class GetSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(): SettingsResult {
        return SettingsResult(
            darkThemeEnabled = repository.isDarkThemeEnabled(),
            notificationsEnabled = repository.areNotificationsEnabled(),
            reminderInterval = repository.getReminderInterval()
        )
    }
}

data class SettingsResult(
    val darkThemeEnabled: Boolean,
    val notificationsEnabled: Boolean,
    val reminderInterval: Int
)