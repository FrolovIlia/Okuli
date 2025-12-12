package com.pixelrabbit.oculi.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "daily_reminder_channel"
        const val NOTIFICATION_ID = 1001
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Daily Reminders"
            val description = "Reminders to complete eye exercises"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                this.description = description
            }

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showReminderNotification() {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Time for eye training!")
            .setContentText("Don't forget your daily exercises")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        )
        notificationManager?.notify(NOTIFICATION_ID, notification)
    }
}