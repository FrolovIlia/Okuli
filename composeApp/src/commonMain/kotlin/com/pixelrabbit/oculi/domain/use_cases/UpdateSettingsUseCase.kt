package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.repositories.SettingsRepository

class UpdateSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(
        darkThemeEnabled: Boolean,
        notificationsEnabled: Boolean,
        reminderEnabled: Boolean,
        reminderInterval: Int
    ) {
        // Сохраняем каждую настройку отдельно
        repository.setDarkThemeEnabled(darkThemeEnabled)
        repository.setNotificationsEnabled(notificationsEnabled)
        repository.setReminderEnabled(reminderEnabled)
        repository.setReminderInterval(reminderInterval)

        println("Все настройки сохранены")
    }
}