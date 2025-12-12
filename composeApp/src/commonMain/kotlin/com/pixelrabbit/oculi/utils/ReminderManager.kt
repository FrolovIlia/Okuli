package com.pixelrabbit.oculi.utils

object ReminderManager {

    fun scheduleEyeCareReminder(intervalMinutes: Int) {
        println("📅 Напоминание установлено: каждые $intervalMinutes минут")
    }

    fun cancelReminders() {
        println("📅 Напоминания отменены")
    }

    fun showBreakReminder() {
        println("⏰ Время сделать перерыв для глаз!")
    }
}