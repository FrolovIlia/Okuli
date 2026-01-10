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
import com.pixelrabbit.oculi.notification.NotificationScheduler
import com.pixelrabbit.oculi.presentation.theme.OculiTheme
import com.pixelrabbit.oculi.presentation.theme.ThemeController
import com.pixelrabbit.oculi.reminder.ReminderStateHolder
import com.pixelrabbit.oculi.utils.platform.AndroidContext
import com.pixelrabbit.oculi.di.ServiceLocator
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        // 🔒 Гарантирует инкремент launchCount только 1 раз на процесс
        private var launchCountIncremented = false
    }

    private val notificationScheduler by lazy { NotificationScheduler(this) }
    private val appOpenAdManager = AppOpenAdManager()
    private var isAdShownInThisSession = false
    private val TAG = "OculiDebug"

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, "Notification permission granted")
                notificationScheduler.scheduleDaily()
            } else {
                Log.d(TAG, "Notification permission denied")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidContext.initialize(applicationContext)

        // ✅ ИНКРЕМЕНТ СЧЁТЧИКА — СТРОГО 1 РАЗ НА ПРОЦЕСС
        if (!launchCountIncremented) {
            launchCountIncremented = true

            lifecycleScope.launch {
                ServiceLocator
                    .settingsRepository()
                    .incrementLaunchCount()

                val count = ServiceLocator
                    .settingsRepository()
                    .getLaunchCount()

                Log.d(TAG, "Launch count incremented once per process. launchCount = $count")
            }
        }

        // === РЕКЛАМА ===
        appOpenAdManager.attachActivity(this)
        appOpenAdManager.initialize {
            Log.d(TAG, "Yandex Ads SDK initialized")
            appOpenAdManager.preloadAd()
        }

        requestNotificationPermissionIfNeeded()

        // === НАПОМИНАНИЯ ===
        lifecycleScope.launch {
            ReminderStateHolder.enabled.collect { enabled ->
                if (enabled) notificationScheduler.scheduleDaily()
                else notificationScheduler.cancelDaily()
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

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val shouldShowAds = ServiceLocator
                .settingsRepository()
                .shouldShowAds()

            if (!shouldShowAds) {
                Log.d(TAG, "Ads disabled by business logic")
                return@launch
            }

            Handler(mainLooper).postDelayed({
                if (!isAdShownInThisSession && appOpenAdManager.isAdAvailable()) {
                    appOpenAdManager.showIfAvailable {
                        Log.d(TAG, "App open ad shown on resume")
                        isAdShownInThisSession = true
                    }
                }
            }, 2000L)
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermissionLauncher.launch(
                android.Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            notificationScheduler.scheduleDaily()
        }
    }
}
