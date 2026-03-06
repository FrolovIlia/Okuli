// androidMain/kotlin/com/pixelrabbit/backy/workers/DailyReminderWorker.kt
package com.pixelrabbit.backy.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pixelrabbit.backy.notification.NotificationHelper

class DailyReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("backyDebug", "DailyReminderWorker: фоновая задача запущена")

        return try {
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.showReminderNotification()
            Log.d("backyDebug", "DailyReminderWorker: уведомление успешно отправлено")
            Result.success()
        } catch (e: Exception) {
            Log.e("backyDebug", "DailyReminderWorker: ошибка при выполнении", e)
            Result.failure()
        }
    }
}