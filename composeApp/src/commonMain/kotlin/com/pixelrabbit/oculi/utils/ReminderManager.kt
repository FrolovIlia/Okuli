package com.pixelrabbit.oculi.utils

object ReminderManager {

    fun scheduleEyeCareReminder(intervalMinutes: Int) {
        println("📅 Напоминание установлено: каждые $intervalMinutes минут")
        // В реальном приложении здесь будет код для планирования уведомлений
    }

    fun cancelReminders() {
        println("📅 Напоминания отменены")
        // В реальном приложении здесь будет код для отмены уведомлений
    }

    fun showBreakReminder() {
        println("⏰ Время сделать перерыв для глаз!")
        // Здесь будет логика показа уведомления
    }
}