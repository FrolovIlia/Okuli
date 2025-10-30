package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.repositories.NotificationRepository

class NotificationRepositoryImpl : NotificationRepository {
    override suspend fun scheduleReminder(intervalMinutes: Int) {
        println("Напоминание установлено на каждые $intervalMinutes минут")
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