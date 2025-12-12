package com.pixelrabbit.oculi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.pixelrabbit.oculi.ads.AppOpenAdManager
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.utils.platform.initializeAndroidContext
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var ads: AppOpenAdManager
    private var shouldShowAd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeAndroidContext(this)

        lifecycleScope.launch {
            shouldShowAd = ServiceLocator.adUseCase.trackAppLaunch()
            val launchCount = ServiceLocator.adUseCase.getLaunchCount()

            println("📱 AppLaunch: Launch #$launchCount, should show ads: $shouldShowAd")
            ads = AppOpenAdManager(this@MainActivity)

            if (shouldShowAd) {
                println("📱 AppLaunch: Loading ad (after 3+ launches)")
                ads.initialize {
                    ads.showIfAvailable {
                        showMainContent()
                    }
                }
            } else {
                println("📱 AppLaunch: Skipping ad (first 2 launches)")
                showMainContent()
            }
        }
    }

    private fun showMainContent() {
        setContent {
            OculiApp()
        }
    }
}