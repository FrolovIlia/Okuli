package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeContent()
    }
}

@Composable
fun HomeContent() {
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Oculi",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Тренировка зрения",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                navigator.push(ExercisesListScreen)
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("💪 Упражнения для глаз")
        }

        Button(
            onClick = {
                navigator.push(VisionTestScreen)
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("👁️ Проверка зрения")
        }

        Button(
            onClick = {
                navigator.push(ProgressScreen)
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("📊 Мой прогресс")
        }

        Button(
            onClick = {
                navigator.push(SettingsScreen)
            },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("⚙️ Настройки")
        }
    }
}