package com.pixelrabbit.backy.notification

expect class NotificationManager {
    fun scheduleDaily(hour: Int, minute: Int)
    fun cancelAll()
}