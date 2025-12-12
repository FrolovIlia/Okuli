package com.pixelrabbit.oculi.notification

import com.pixelrabbit.oculi.di.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IOSNotificationManager {

    fun scheduleDailyReminder() {
        // Вызов нативной реализации в Swift
    }

    fun updateLastVisitOnAppActive() {
        CoroutineScope(Dispatchers.Default).launch {
            ServiceLocator.lastVisitRepository().updateLastVisit()
        }
    }
}