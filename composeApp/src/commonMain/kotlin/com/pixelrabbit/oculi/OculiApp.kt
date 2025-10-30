package com.pixelrabbit.oculi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.pixelrabbit.oculi.presentation.screens.OnboardingScreen
import com.pixelrabbit.oculi.presentation.theme.OculiTheme

@Composable
fun OculiApp() {
    var darkTheme by remember { mutableStateOf(false) }

    OculiTheme(darkTheme = darkTheme) {
        Navigator(OnboardingScreen) { navigator ->
            SlideTransition(navigator)
        }
    }
}