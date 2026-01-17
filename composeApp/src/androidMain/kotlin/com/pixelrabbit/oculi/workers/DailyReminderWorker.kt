// androidMain/kotlin/com/pixelrabbit/oculi/workers/DailyReminderWorker.kt
package com.pixelrabbit.oculi.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pixelrabbit.oculi.notification.NotificationHelper

class DailyReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("OculiDebug", "DailyReminderWorker: фоновая задача запущена")

        return try {
            val notificationHelper = NotificationHelper(applicationContext)
            notificationHelper.showReminderNotification()
            Log.d("OculiDebug", "DailyReminderWorker: уведомление успешно отправлено")
            Result.success()
        } catch (e: Exception) {
            Log.e("OculiDebug", "DailyReminderWorker: ошибка при выполнении", e)
            Result.failure()
        }
    }
}