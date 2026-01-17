// composeApp/src/androidMain/kotlin/com/pixelrabbit/oculi/notification/NotificationHelper.kt
package com.pixelrabbit.oculi.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pixelrabbit.oculi.MainActivity
import com.pixelrabbit.oculi.R // Импорт ресурсов твоего приложения

class NotificationHelper(private val context: Context) {

    private val channelId = "oculi_notifications"

    fun showReminderNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Oculi Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification) // ТВОЯ ИКОНКА
            .setContentTitle("От Oculi")
            .setContentText("Пора сделать зарядку для глаз!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}