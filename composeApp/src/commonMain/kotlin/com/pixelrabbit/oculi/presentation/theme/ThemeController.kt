package com.pixelrabbit.oculi.presentation.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeController {
    private val _themeState = MutableStateFlow(false) // false = светлая
    val themeState: StateFlow<Boolean> = _themeState.asStateFlow()

    fun setDarkTheme(enabled: Boolean) {
        _themeState.value = enabled
    }
}
