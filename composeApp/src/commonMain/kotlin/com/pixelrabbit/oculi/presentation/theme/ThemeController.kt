package com.pixelrabbit.oculi.presentation.theme

import com.pixelrabbit.oculi.di.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ThemeController {
    private val _themeState = MutableStateFlow(false)
    val themeState: StateFlow<Boolean> = _themeState.asStateFlow()

    init {
        // Загружаем сохраненную тему при создании контроллера
        CoroutineScope(Dispatchers.Main).launch {
            val savedTheme = ServiceLocator.getSettingsUseCase().darkThemeEnabled
            _themeState.value = savedTheme
            println("Тема загружена при инициализации: ${if (savedTheme) "темная" else "светлая"}")
        }
    }

    fun setDarkTheme(enabled: Boolean) {
        _themeState.value = enabled

        // Сохраняем в настройки
        CoroutineScope(Dispatchers.Main).launch {
            ServiceLocator.settingsRepository().setDarkThemeEnabled(enabled)
        }
    }
}