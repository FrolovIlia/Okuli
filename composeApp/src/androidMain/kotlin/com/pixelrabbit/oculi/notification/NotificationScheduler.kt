package com.pixelrabbit.oculi.notification

import android.content.Context
import androidx.work.*
import com.pixelrabbit.oculi.workers.DailyReminderWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    private val workManager = WorkManager.getInstance(context)

    fun scheduleDaily() {
        cancelDaily()

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        var delay = calendar.timeInMillis - System.currentTimeMillis()
        if (delay < 0) delay += TimeUnit.DAYS.toMillis(1)

        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelDaily() {
        workManager.cancelUniqueWork("daily_reminder")
    }
}
