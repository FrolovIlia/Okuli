package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.di.ServiceLocator

object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        SettingsContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent() {
    val navigator = LocalNavigator.currentOrThrow

    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkTheme by remember { mutableStateOf(false) }
    var reminderInterval by remember { mutableStateOf(60) }
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(notificationsEnabled, reminderInterval) {
        ServiceLocator.manageNotificationsUseCase(
            enabled = notificationsEnabled,
            intervalMinutes = reminderInterval
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Уведомления
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🔔 Уведомления",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingSwitch(
                        text = "Включить уведомления",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )

                    SettingSwitch(
                        text = "Звук уведомлений",
                        checked = soundEnabled,
                        onCheckedChange = { soundEnabled = it },
                        enabled = notificationsEnabled
                    )

                    SettingSwitch(
                        text = "Вибрация",
                        checked = vibrationEnabled,
                        onCheckedChange = { vibrationEnabled = it },
                        enabled = notificationsEnabled
                    )
                }
            }

            // Внешний вид
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎨 Внешний вид",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SettingSwitch(
                        text = "Темная тема",
                        checked = darkTheme,
                        onCheckedChange = { darkTheme = it }
                    )
                }
            }

            // Напоминания
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "⏰ Напоминания",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Интервал напоминаний: $reminderInterval минут",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(30, 60, 90, 120).forEach { interval ->
                            Button(
                                onClick = { reminderInterval = interval },
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                                enabled = reminderInterval != interval
                            ) {
                                Text("$interval")
                            }
                        }
                    }
                }
            }

            // О приложении
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "ℹ️ О приложении",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Oculi v1.0.0",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Тренировка зрения и снятие цифрового напряжения",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📄 Политика конфиденциальности")
                    }

                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📝 Условия использования")
                    }

                    Button(
                        onClick = { /* TODO: Поделиться приложением */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("📤 Поделиться приложением")
                    }
                }
            }

            Button(
                onClick = { navigator.pop() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Назад")
            }
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
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}