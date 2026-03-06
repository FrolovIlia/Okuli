package com.pixelrabbit.backy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.pixelrabbit.backy.ads.AppOpenAdManager
import com.pixelrabbit.backy.presentation.screens.HomeScreen
import com.pixelrabbit.backy.presentation.screens.OnboardingScreen
import com.pixelrabbit.backy.presentation.theme.ThemeController
import com.pixelrabbit.backy.utils.AppSettings
import kotlinx.coroutines.delay

@Composable
fun backyApp(appOpenAdManager: AppOpenAdManager? = null) {
    var shouldShowOnboarding by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }
    var showSplash by remember { mutableStateOf(true) }
    var pendingAdAvailable by remember { mutableStateOf(false) }
    var adChecked by remember { mutableStateOf(false) }

    val darkTheme by ThemeController.themeState.collectAsState()
    val appSettings = remember { AppSettings() }

    LaunchedEffect(Unit) {
        shouldShowOnboarding = !appSettings.isOnboardingCompleted
        isLoading = false
    }

    LaunchedEffect(appOpenAdManager, showSplash) {
        if (appOpenAdManager != null && showSplash && !adChecked) {
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
        } else if (appOpenAdManager == null && showSplash) {
            delay(1500L)
            showSplash = false
        }
    }

    if (isLoading) {
        LoadingScreen()
    } else if (showSplash) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Загрузка...", color = MaterialTheme.colorScheme.onBackground)
        }
    } else {
        Navigator(screen = if (shouldShowOnboarding) OnboardingScreen else HomeScreen)

        if (pendingAdAvailable && appOpenAdManager != null) {
            LaunchedEffect(Unit) {
                delay(100L)
                appOpenAdManager.showIfAvailable {
                }
                pendingAdAvailable = false
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Загрузка...", color = MaterialTheme.colorScheme.onBackground)
    }
}