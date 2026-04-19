package com.pixelrabbit.backy

import android.content.Intent
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
import com.pixelrabbit.backy.ads.AppOpenAdManager
import com.pixelrabbit.backy.notification.NotificationManager
import com.pixelrabbit.backy.payment.PaymentController
import com.pixelrabbit.backy.payment.PaymentResultHandler
import com.pixelrabbit.backy.presentation.theme.ThemeController
import com.pixelrabbit.backy.presentation.theme.backyTheme
import com.pixelrabbit.backy.reminder.ReminderStateHolder
import com.pixelrabbit.backy.utils.platform.AndroidContext
import com.pixelrabbit.backy.di.ServiceLocator
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    companion object {
        private var launchCountIncremented = false
    }

    private val appOpenAdManager = AppOpenAdManager()
    private val paymentController = PaymentController()
    private var isAdShownInThisSession = false
    private val TAG = "backyDebug"

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

        val config = AppMetricaConfig.newConfigBuilder("b524cd64-b8a1-4134-8169-e50cb665c359").build()
        AppMetrica.activate(this, config)

        AndroidContext.initialize(applicationContext)
        ServiceLocator.init(NotificationManager(this))

        if (!launchCountIncremented) {
            launchCountIncremented = true
            lifecycleScope.launch {
                ServiceLocator.settingsRepository().incrementLaunchCount()
            }
        }

        appOpenAdManager.attachActivity(this)
        appOpenAdManager.initialize {
            appOpenAdManager.preloadAd()
        }

        paymentController.setActivity(this)

        requestNotificationPermissionIfNeeded()

        lifecycleScope.launch {
            ReminderStateHolder.enabled.collect { isEnabled ->
                Log.d(TAG, "Reminder state changed: $isEnabled")
                syncNotifications()
            }
        }

        setContent {
            val darkTheme by ThemeController.themeState.collectAsState()
            backyTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    backyApp(appOpenAdManager = appOpenAdManager)
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PaymentController.REQUEST_CODE_TOKENIZE) {
            if (resultCode == RESULT_OK && data != null) {
                val token = data.getStringExtra("ru.yoomoney.sdk.kassa.payments.extra.PAYMENT_TOKEN")
                if (!token.isNullOrEmpty()) {
                    PaymentResultHandler.onTokenReceived?.invoke(token)
                } else {
                    PaymentResultHandler.onCancelled?.invoke()
                }
            } else {
                PaymentResultHandler.onCancelled?.invoke()
            }
        }

        if (requestCode == PaymentController.REQUEST_CODE_3DS) {
            PaymentResultHandler.on3DSCompleted?.invoke(resultCode == RESULT_OK)
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
            val launchCount = ServiceLocator.settingsRepository().getLaunchCount()
            val shouldShowAds = ServiceLocator.settingsRepository().shouldShowAds()
            val adsRemoved = ServiceLocator.settingsRepository().isAdsRemoved()

            if (launchCount >= 3 && shouldShowAds && !adsRemoved && !isAdShownInThisSession) {
                Handler(mainLooper).postDelayed({
                    if (appOpenAdManager.isAdAvailable()) {
                        appOpenAdManager.showIfAvailable {
                            isAdShownInThisSession = true
                        }
                    }
                }, 2000L)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appOpenAdManager.unregisterNetworkCallback()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            syncNotifications()
        }
    }
}