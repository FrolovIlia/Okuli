package com.pixelrabbit.oculi.reminder

import com.pixelrabbit.oculi.di.ServiceLocator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ReminderStateHolder {

    private val _enabled = MutableStateFlow(false)
    val enabled: StateFlow<Boolean> = _enabled.asStateFlow()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            val saved = ServiceLocator.getSettingsUseCase().reminderEnabled
            _enabled.value = saved
            println("Напоминания загружены при инициализации: $saved")
        }
    }

    fun setEnabled(value: Boolean) {
        _enabled.value = value

        CoroutineScope(Dispatchers.Main).launch {
            ServiceLocator.settingsRepository().setReminderEnabled(value)
        }
    }
}
