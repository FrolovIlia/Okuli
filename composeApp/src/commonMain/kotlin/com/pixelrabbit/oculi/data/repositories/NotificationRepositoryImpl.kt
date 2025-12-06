package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.repositories.NotificationRepository

class NotificationRepositoryImpl : NotificationRepository {
    override suspend fun scheduleReminder(intervalHours: Int) {
        val hoursText = when {
            intervalHours % 10 == 1 && intervalHours % 100 != 11 -> "час"
            intervalHours % 10 in 2..4 && intervalHours % 100 !in 12..14 -> "часа"
            else -> "часов"
        }
        println("Напоминание установлено на каждые $intervalHours $hoursText")
        // TODO: Реализовать реальные уведомления
    }

    override suspend fun cancelReminders() {
        println("Напоминания отменены")
        // TODO: Реализовать отмену уведомлений
    }

    override suspend fun isNotificationEnabled(): Boolean {
        return true // TODO: Получить из настроек
    }

    override suspend fun setNotificationEnabled(enabled: Boolean) {
        println("Уведомления ${if (enabled) "включены" else "отключены"}")
        // TODO: Сохранить в настройки
    }
}