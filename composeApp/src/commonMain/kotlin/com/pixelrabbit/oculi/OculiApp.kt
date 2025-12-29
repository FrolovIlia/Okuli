package com.pixelrabbit.oculi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.presentation.screens.HomeScreen
import com.pixelrabbit.oculi.presentation.screens.OnboardingScreen
import com.pixelrabbit.oculi.presentation.theme.ThemeController
import com.pixelrabbit.oculi.utils.AppSettings

@Composable
fun OculiApp() {
    var shouldShowOnboarding by remember { androidx.compose.runtime.mutableStateOf(true) }
    var isLoading by remember { androidx.compose.runtime.mutableStateOf(true) }

    val darkTheme by ThemeController.themeState.collectAsState()
    val appSettings = remember { AppSettings() }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        shouldShowOnboarding = !appSettings.isOnboardingCompleted
        isLoading = false
    }

    if (isLoading) {
        LoadingScreen()
    } else {
        Navigator(screen = if (shouldShowOnboarding) OnboardingScreen else HomeScreen)
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
