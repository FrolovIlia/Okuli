// shared/src/commonMain/kotlin/com/pixelrabbit/backy/presentation/screens/SettingsScreen.kt
package com.pixelrabbit.backy.presentation.screens

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
import com.pixelrabbit.backy.AppInfo
import com.pixelrabbit.backy.di.ServiceLocator
import com.pixelrabbit.backy.presentation.theme.ThemeController
import com.pixelrabbit.backy.reminder.ReminderStateHolder
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
            // Внешний вид
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Внешний вид", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    SettingSwitch(
                        text = "Темная тема",
                        checked = darkTheme,
                        onCheckedChange = { ThemeController.setDarkTheme(it) }
                    )
                }
            }

            // Напоминания
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Напоминания", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    SettingSwitch(
                        text = "Ежедневные напоминания (20:00)",
                        checked = reminderEnabled,
                        onCheckedChange = { enabled ->
                            // Только меняем стейт. MainActivity или AppDelegate подхватят изменение
                            // и вызовут NotificationManager.schedule/cancel
                            ReminderStateHolder.setEnabled(enabled)

                            scope.launch {
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

            Button(
                onClick = { navigator.pop() },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Назад")
            }
        }
    }
}

@Composable
fun SettingSwitch(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}