package com.pixelrabbit.oculi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import cafe.adriel.voyager.navigator.Navigator
import com.pixelrabbit.oculi.presentation.screens.HomeScreen
import com.pixelrabbit.oculi.presentation.screens.OnboardingScreen
import com.pixelrabbit.oculi.presentation.theme.OculiTheme
import com.pixelrabbit.oculi.presentation.theme.ThemeController
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.utils.AppSettings

@Composable
fun OculiApp() {
    var shouldShowOnboarding by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }

    val appSettings = remember { AppSettings() }
    val darkTheme by ThemeController.themeState.collectAsState()

    LaunchedEffect(Unit) {
        val settings = ServiceLocator.getSettingsUseCase()
        ThemeController.setDarkTheme(settings.darkThemeEnabled)
        shouldShowOnboarding = !appSettings.isOnboardingCompleted
        isLoading = false
    }

    OculiTheme(darkTheme = darkTheme) {
        if (isLoading) {
            LoadingScreen()
        } else {
            Navigator(screen = if (shouldShowOnboarding) OnboardingScreen else HomeScreen)
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Загрузка...")
    }
}
