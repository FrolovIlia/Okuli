// shared/src/iosMain/kotlin/com/pixelrabbit/backy/notification/NotificationManager.kt
package com.pixelrabbit.backy.notification

import platform.UserNotifications.*
import platform.Foundation.*

actual class NotificationManager {

    actual fun scheduleDaily(hour: Int, minute: Int) {
        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.requestAuthorizationWithOptions(UNAuthorizationOptionAlert or UNAuthorizationOptionSound) { granted, error ->
            if (granted) {
                val content = UNMutableNotificationContent().apply {
                    setTitle("Пора тренировать зрение!")
                    setBody("Не забывайте о ежедневных упражнениях")
                    setSound(UNNotificationSound.defaultSound())
                }

                val dateComponents = NSDateComponents().apply {
                    this.hour = hour.toLong()
                    this.minute = minute.toLong()
                }

                val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(dateComponents, repeats = true)
                val request = UNNotificationRequest.requestWithIdentifier("daily_reminder", content, trigger)

                center.addNotificationRequest(request) { err ->
                    err?.let { println("iOS Notification Error: $it") }
                }
            }
        }
    }

    actual fun cancelAll() {
        UNUserNotificationCenter.currentNotificationCenter().removeAllPendingNotificationRequests()
        UNUserNotificationCenter.currentNotificationCenter().removeAllDeliveredNotifications()
    }
}