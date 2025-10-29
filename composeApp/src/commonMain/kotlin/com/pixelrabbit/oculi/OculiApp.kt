package com.pixelrabbit.oculi

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.pixelrabbit.oculi.presentation.screens.OnboardingScreen
import com.pixelrabbit.oculi.presentation.theme.OculiTheme

@Composable
fun OculiApp() {
    OculiTheme {
        Navigator(OnboardingScreen) { navigator ->
            SlideTransition(navigator)
        }
    }
}