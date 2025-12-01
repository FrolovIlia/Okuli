package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.AppInfo
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.presentation.theme.ThemeController
import kotlinx.coroutines.launch

object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        SettingsContent()
    }
}

@Composable
fun SettingsContent() {
    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()

    // Используем тему из ThemeController
    val darkTheme by ThemeController.themeState.collectAsState()

    // Локальные состояния для остальных настроек
    var notificationsEnabled by remember { mutableStateOf(true) }
    var reminderEnabled by remember { mutableStateOf(true) }
    var reminderInterval by remember { mutableStateOf(60) }

    // Загружаем сохраненные настройки при запуске
    LaunchedEffect(Unit) {
        val settings = ServiceLocator.getSettingsUseCase()
        notificationsEnabled = settings.notificationsEnabled
        reminderEnabled = settings.reminderEnabled
        reminderInterval = settings.reminderInterval

        println("Настройки загружены из хранилища:")
        println("- Тема: ${if (settings.darkThemeEnabled) "темная" else "светлая"}")
        println("- Уведомления: ${if (settings.notificationsEnabled) "вкл" else "выкл"}")
        println("- Напоминания: ${if (settings.reminderEnabled) "вкл" else "выкл"}")
        println("- Интервал: ${settings.reminderInterval} мин")
    }

    // Сохраняем настройки при изменении
    LaunchedEffect(notificationsEnabled, reminderEnabled, reminderInterval) {
        scope.launch {
            ServiceLocator.updateSettingsUseCase(
                darkThemeEnabled = darkTheme,
                notificationsEnabled = notificationsEnabled,
                reminderEnabled = reminderEnabled,
                reminderInterval = reminderInterval
            )

            // Обновляем уведомления
            val shouldShowNotifications = notificationsEnabled && reminderEnabled
            ServiceLocator.manageNotificationsUseCase(
                enabled = shouldShowNotifications,
                intervalMinutes = reminderInterval
            )
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Внешний вид
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Внешний вид",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SettingSwitch(
                        text = "Темная тема",
                        checked = darkTheme,
                        onCheckedChange = { ThemeController.setDarkTheme(it) }
                    )
                }
            }

            // Уведомления
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Уведомления",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SettingSwitch(
                        text = "Включить уведомления",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
            }

            // Напоминания
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Напоминания",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SettingSwitch(
                        text = "Включить напоминания",
                        checked = reminderEnabled,
                        onCheckedChange = { reminderEnabled = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Интервал напоминаний (можно добавить позже)
                    Text(
                        text = "Интервал: $reminderInterval минут",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // О приложении
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Oculi v${AppInfo.versionName}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Тренировка зрения и снятие цифрового напряжения",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Кнопка Назад
            Button(
                onClick = { navigator.pop() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Назад")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SettingSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}
