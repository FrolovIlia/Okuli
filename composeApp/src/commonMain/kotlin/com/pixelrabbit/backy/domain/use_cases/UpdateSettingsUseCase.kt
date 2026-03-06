package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.domain.repositories.SettingsRepository

class UpdateSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(
        darkThemeEnabled: Boolean,
        notificationsEnabled: Boolean,
        reminderEnabled: Boolean,
        reminderInterval: Int
    ) {
        repository.setDarkThemeEnabled(darkThemeEnabled)
        repository.setNotificationsEnabled(notificationsEnabled)
        repository.setReminderEnabled(reminderEnabled)
        repository.setReminderInterval(reminderInterval)

        println("Все настройки сохранены")
    }
}