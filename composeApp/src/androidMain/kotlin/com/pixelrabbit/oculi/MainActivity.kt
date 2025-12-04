package com.pixelrabbit.oculi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pixelrabbit.oculi.ads.AppOpenAdManager
import com.pixelrabbit.oculi.utils.platform.initializeAndroidContext

class MainActivity : ComponentActivity() {

    private lateinit var ads: AppOpenAdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⚡ Инициализация глобального androidContext для KMP
        initializeAndroidContext(this)

        // Инициализация рекламы
        ads = AppOpenAdManager(this)
        ads.initialize {
            // UI запускается безопасно после показа рекламы
            setContent {
                OculiApp()
            }
        }
    }
}
