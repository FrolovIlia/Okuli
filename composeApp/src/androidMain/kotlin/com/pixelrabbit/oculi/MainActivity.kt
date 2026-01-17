// androidMain/kotlin/com/pixelrabbit/oculi/MainActivity.kt
package com.pixelrabbit.oculi

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.pixelrabbit.oculi.ads.AppOpenAdManager
import com.pixelrabbit.oculi.notification.NotificationManager
import com.pixelrabbit.oculi.presentation.theme.ThemeController
import com.pixelrabbit.oculi.presentation.theme.OculiTheme
import com.pixelrabbit.oculi.reminder.ReminderStateHolder
import com.pixelrabbit.oculi.utils.platform.AndroidContext
import com.pixelrabbit.oculi.di.ServiceLocator
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        private var launchCountIncremented = false
    }

    private val appOpenAdManager = AppOpenAdManager()
    private var isAdShownInThisSession = false
    private val TAG = "OculiDebug"

    // Регистрация лаунчера разрешений
    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, "Notification permission granted")
                syncNotifications()
            } else {
                Log.w(TAG, "Notification permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Инициализация контекста и ServiceLocator
        AndroidContext.initialize(applicationContext)
        ServiceLocator.init(NotificationManager(this))

        // 2. Инкремент счетчика запусков
        if (!launchCountIncremented) {
            launchCountIncremented = true
            lifecycleScope.launch {
                ServiceLocator.settingsRepository().incrementLaunchCount()
            }
        }

        // 3. Реклама
        appOpenAdManager.attachActivity(this)
        appOpenAdManager.initialize {
            appOpenAdManager.preloadAd()
        }

        // 4. Разрешения и синхронизация уведомлений
        requestNotificationPermissionIfNeeded()

        // Слушаем изменения стейта, чтобы перепланировать уведомления
        lifecycleScope.launch {
            ReminderStateHolder.enabled.collect { isEnabled ->
                Log.d(TAG, "Reminder state changed: $isEnabled")
                syncNotifications()
            }
        }

        setContent {
            val darkTheme by ThemeController.themeState.collectAsState()
            OculiTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OculiApp(appOpenAdManager = appOpenAdManager)
                }
            }
        }
    }

    private fun syncNotifications() {
        val manager = ServiceLocator.getNotificationManager()
        if (ReminderStateHolder.enabled.value) {
            Log.d(TAG, "Syncing notifications: Scheduling for 20:00")
            manager.scheduleDaily(20, 0)
        } else {
            Log.d(TAG, "Syncing notifications: Cancelling all")
            manager.cancelAll()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            if (ServiceLocator.settingsRepository().shouldShowAds()) {
                Handler(mainLooper).postDelayed({
                    if (!isAdShownInThisSession && appOpenAdManager.isAdAvailable()) {
                        appOpenAdManager.showIfAvailable { isAdShownInThisSession = true }
                    }
                }, 2000L)
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            syncNotifications()
        }
    }
}