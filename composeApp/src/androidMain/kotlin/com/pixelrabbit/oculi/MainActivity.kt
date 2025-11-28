package com.pixelrabbit.oculi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.pixelrabbit.oculi.ad.AdManager
import com.pixelrabbit.oculi.utils.platform.initializeAndroidContext

class MainActivity : ComponentActivity() {
    private val adManager = AdManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeAndroidContext(this)
        adManager.setContext(this)

        setContent {
            OculiAppWithAds(adManager)
        }
    }

    override fun onResume() {
        super.onResume()
        // Показываем рекламу при каждом открытии приложения
        adManager.showInterstitialAd()
    }
}

@Composable
fun OculiAppWithAds(adManager: AdManager) {
    LaunchedEffect(Unit) {
        adManager.initializeSdk()
        // Пока тестируем только баннер
        println("Ads initialized - ready for banner")
    }

    OculiApp()
}

@Preview
@Composable
fun AppAndroidPreview() {
    OculiApp()
}