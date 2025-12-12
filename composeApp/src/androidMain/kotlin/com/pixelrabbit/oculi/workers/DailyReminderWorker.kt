package com.pixelrabbit.oculi.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.notification.NotificationHelper

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val shouldNotify = ServiceLocator.checkDailyVisitUseCase.execute()

            if (shouldNotify) {
                NotificationHelper(applicationContext).showReminderNotification()
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}