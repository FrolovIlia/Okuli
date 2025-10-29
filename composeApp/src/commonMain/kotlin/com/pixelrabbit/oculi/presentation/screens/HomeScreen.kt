package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeContent()
    }
}

@Composable
fun HomeContent(
    onExerciseClick: (String) -> Unit = {},
    onTestClick: () -> Unit = {},
    onProgressClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Главный экран Oculi")
        Button(
            onClick = { onExerciseClick("exercise1") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Начать тренировку")
        }
        Button(
            onClick = onTestClick,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Проверить зрение")
        }
        Button(
            onClick = onProgressClick,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Мой прогресс")
        }
    }
}