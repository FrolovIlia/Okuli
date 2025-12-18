package com.pixelrabbit.oculi.notification

import android.content.Context
import androidx.work.*
import com.pixelrabbit.oculi.workers.DailyReminderWorker
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationScheduler(private val context: Context) {

    fun scheduleDailyCheck() {
        val workManager = WorkManager.getInstance(context)

        workManager.cancelUniqueWork("daily_reminder")

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        var initialDelay = calendar.timeInMillis - System.currentTimeMillis()
        if (initialDelay < 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            initialDelay = calendar.timeInMillis - System.currentTimeMillis()
        }

        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.NONE)
            .addTag("daily_reminder")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }
}
