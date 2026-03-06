package com.pixelrabbit.backy

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pixelrabbit.backy.ads.AppOpenAdManager
import kotlinx.coroutines.delay

@Composable
fun AndroidbackyApp(appOpenAdManager: AppOpenAdManager) {
    var showSplash by remember { mutableStateOf(true) }
    var pendingAdAvailable by remember { mutableStateOf(false) }
    var adChecked by remember { mutableStateOf(false) }

    LaunchedEffect(appOpenAdManager, showSplash) {
        if (showSplash && !adChecked) {
            val maxWaitTime = 3000L
            val checkInterval = 100L
            var waitedTime = 0L

            while (waitedTime < maxWaitTime &&
                !appOpenAdManager.isAdAvailable() &&
                showSplash) {
                delay(checkInterval)
                waitedTime += checkInterval
            }

            adChecked = true

            val canShowAd = appOpenAdManager.isAdAvailable()

            if (canShowAd) {
                pendingAdAvailable = true
            }

            delay(500L)
            showSplash = false
        } else if (showSplash) {
            delay(1500L)
            showSplash = false
        }
    }

    if (showSplash) {
        SplashScreenContent()
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            backyApp()

            Button(
                onClick = {
                    android.util.Log.d("backyDebug", "Тестовая кнопка нажата")
                    if (appOpenAdManager.isAdAvailable()) {
                        appOpenAdManager.showIfAvailable {
                            android.util.Log.d("backyDebug", "Колбэк после рекламы (кнопка)")
                        }
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Тест: Показать рекламу")
            }
        }

        if (pendingAdAvailable) {
            LaunchedEffect(Unit) {
                delay(100L)
                appOpenAdManager.showIfAvailable {
                    android.util.Log.d("backyDebug", "Реклама показана после Splash")
                }
                pendingAdAvailable = false
            }
        }
    }
}

@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_standart),
                contentDescription = "Логотип backy",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Загрузка...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}