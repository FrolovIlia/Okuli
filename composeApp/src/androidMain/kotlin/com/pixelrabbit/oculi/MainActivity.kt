package com.pixelrabbit.oculi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pixelrabbit.oculi.ads.AppOpenAdManager
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.notification.NotificationScheduler
import com.pixelrabbit.oculi.presentation.theme.OculiTheme
import com.pixelrabbit.oculi.utils.platform.AndroidContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val notificationScheduler by lazy { NotificationScheduler(this) }
    private val adManager by lazy { AppOpenAdManager(this) }
    private val TAG = "OculiDebug"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "1. onCreate started")

        AndroidContext.Companion.initialize(applicationContext)
        Log.d(TAG, "AndroidContext initialized")

        try {
            // 1. Обновление даты посещения
            Log.d(TAG, "2. Updating last visit date")
            CoroutineScope(Dispatchers.IO).launch {
                ServiceLocator.lastVisitRepository().updateLastVisit()
            }

            // 2. Планирование уведомлений
            Log.d(TAG, "3. Scheduling notifications")
            notificationScheduler.scheduleDailyCheck()

            // 3. Реклама при запуске
            Log.d(TAG, "4. Initializing ad")
            CoroutineScope(Dispatchers.IO).launch {
                val shouldShowAd = ServiceLocator.adUseCase.trackAppLaunch()
                Log.d(TAG, "Should show ad: $shouldShowAd")

                if (shouldShowAd) {
                    // Показываем рекламу
                    runOnUiThread {
                        showAdAndThenMainUI()
                    }
                } else {
                    // Пропускаем рекламу
                    runOnUiThread {
                        showMainUI()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "CRASH in onCreate: ${e.message}", e)
            showMainUI() // Если ошибка, показываем основной UI
        }
    }

    private fun showAdAndThenMainUI() {
        Log.d(TAG, "5. Loading ad")
        adManager.initialize {
            Log.d(TAG, "6. Ad loaded, showing...")
            adManager.showIfAvailable {
                Log.d(TAG, "7. Ad dismissed, showing main UI")
                showMainUI()
            }
        }
    }

    private fun showMainUI() {
        Log.d(TAG, "8. Showing main UI")
        setContent {
            OculiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Log.d(TAG, "9. Creating OculiApp")
                    OculiApp()
                }
            }
        }
        Log.d(TAG, "10. onCreate completed successfully")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume - updating last visit")
        CoroutineScope(Dispatchers.IO).launch {
            ServiceLocator.lastVisitRepository().updateLastVisit()
        }
    }
}