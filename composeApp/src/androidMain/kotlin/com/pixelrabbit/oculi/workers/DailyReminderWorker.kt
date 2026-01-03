package com.pixelrabbit.oculi.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pixelrabbit.oculi.notification.NotificationHelper
import com.pixelrabbit.oculi.reminder.ReminderStateHolder
import kotlinx.coroutines.flow.first

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val enabled = ReminderStateHolder.enabled.first()
            if (!enabled) return Result.success()

            NotificationHelper(applicationContext).showReminderNotification()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
