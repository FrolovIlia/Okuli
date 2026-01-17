// androidMain/kotlin/com/pixelrabbit/oculi/notification/NotificationManager.kt
package com.pixelrabbit.oculi.notification

import android.content.Context
import android.util.Log
import androidx.work.*
import com.pixelrabbit.oculi.workers.DailyReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

actual class NotificationManager(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)
    private val TAG = "OculiDebug"

    actual fun scheduleDaily(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        val delay = calendar.timeInMillis - System.currentTimeMillis()

        val periodicRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag("daily_reminder_tag")
            .build()

        // Используем KEEP: если задача уже есть, она продолжит ждать своего времени
        workManager.enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )

        Log.d(TAG, "NotificationManager: запланировано на $hour:$minute. Задержка: ${delay / 1000 / 60} мин.")
    }

    actual fun cancelAll() {
        Log.d(TAG, "NotificationManager: отмена всех уведомлений")
        workManager.cancelUniqueWork("daily_reminder")
    }
}