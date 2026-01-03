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
import com.pixelrabbit.oculi.reminder.ReminderStateHolder
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

    val darkTheme by ThemeController.themeState.collectAsState()
    val reminderEnabled by ReminderStateHolder.enabled.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            // --- Внешний вид ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Внешний вид",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SettingSwitch(
                        text = "Темная тема",
                        checked = darkTheme,
                        onCheckedChange = { ThemeController.setDarkTheme(it) }
                    )
                }
            }

            // --- Напоминания ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Напоминания",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SettingSwitch(
                        text = "Ежедневные напоминания",
                        checked = reminderEnabled,
                        onCheckedChange = { enabled ->
                            ReminderStateHolder.setEnabled(enabled)

                            scope.launch {
                                // Логика включения/выключения уведомлений
                                ServiceLocator.manageNotificationsUseCase(
                                    enabled = enabled,
                                    intervalHours = 24
                                )

                                ServiceLocator.updateSettingsUseCase(
                                    darkThemeEnabled = darkTheme,
                                    notificationsEnabled = enabled,
                                    reminderEnabled = enabled,
                                    reminderInterval = 24
                                )
                            }
                        }
                    )
                }
            }

            // --- О приложении ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Oculi v${AppInfo.versionName}",
                        style = MaterialTheme.typography.titleMedium
                    )
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
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
