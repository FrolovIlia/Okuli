package com.pixelrabbit.oculi.notification

expect class NotificationManager {
    fun scheduleDaily(hour: Int, minute: Int)
    fun cancelAll()
}