package com.pixelrabbit.oculi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.utils.ReminderManager

@Composable
fun AppStateManager() {
    var settings by remember {
        mutableStateOf(
            com.pixelrabbit.oculi.domain.use_cases.SettingsResult(
                darkThemeEnabled = false,
                notificationsEnabled = true,
                reminderEnabled = true, // ДОБАВЬТЕ ЭТОТ ПАРАМЕТР
                reminderInterval = 60
            )
        )
    }

    // Загружаем настройки при запуске
    LaunchedEffect(Unit) {
        settings = ServiceLocator.getSettingsUseCase()

        // Настраиваем напоминания
        if (settings.notificationsEnabled && settings.reminderEnabled) { // Учитываем оба флага
            ReminderManager.scheduleEyeCareReminder(settings.reminderInterval)
        }
    }

    // Следим за изменениями настроек
    LaunchedEffect(settings) {
        if (settings.notificationsEnabled && settings.reminderEnabled) { // Учитываем оба флага
            ReminderManager.scheduleEyeCareReminder(settings.reminderInterval)
        } else {
            ReminderManager.cancelReminders()
        }
    }
}