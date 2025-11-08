// OculiApp.kt
package com.pixelrabbit.oculi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.pixelrabbit.oculi.presentation.screens.HomeScreen
import com.pixelrabbit.oculi.presentation.screens.OnboardingScreen
import com.pixelrabbit.oculi.presentation.theme.OculiTheme
import com.pixelrabbit.oculi.utils.AppSettings
import com.pixelrabbit.oculi.di.ServiceLocator

@Composable
fun OculiApp() {
    var darkTheme by remember { mutableStateOf(false) }
    var shouldShowOnboarding by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(true) }

    val appSettings = remember { AppSettings() }

    LaunchedEffect(Unit) {
        // Загружаем настройки темы
        val settings = ServiceLocator.getSettingsUseCase()
        darkTheme = settings.darkThemeEnabled

        // Проверяем, нужно ли показывать онбординг
        shouldShowOnboarding = !appSettings.isOnboardingCompleted
        isLoading = false
    }

    OculiTheme(darkTheme = darkTheme) {
        if (isLoading) {
            // Простой лоадер
            LoadingScreen()
        } else {
            Navigator(
                screen = if (shouldShowOnboarding) OnboardingScreen else HomeScreen
            ) { navigator ->
                SlideTransition(navigator)
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
        Text("Загрузка...")
    }
}