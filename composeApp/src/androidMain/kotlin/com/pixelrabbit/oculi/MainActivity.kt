package com.pixelrabbit.oculi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.pixelrabbit.oculi.utils.platform.initializeAndroidContext

// android/src/androidMain/kotlin/MainActivity.kt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализируем контекст для всего приложения
        initializeAndroidContext(this)

        setContent {
            OculiApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    OculiApp()
}