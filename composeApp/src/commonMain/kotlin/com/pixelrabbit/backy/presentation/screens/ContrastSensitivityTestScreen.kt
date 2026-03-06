package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object ContrastSensitivityTestScreen : Screen {
    @Composable
    override fun Content() {
        ContrastTestContent()
    }
}

@Composable
fun ContrastTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    var currentStep by remember { mutableStateOf(0) }
    var testCompleted by remember { mutableStateOf(false) }
    var userCanSee by remember { mutableStateOf(true) } // Начнем с true
    var correctAnswers by remember { mutableStateOf(0) }

    val steps = 20
    val contrasts = List(steps) { index -> 0.7f - index * (0.65f / (steps - 1)) }

    var currentMode by remember { mutableStateOf(ContrastMode.DARK_ON_LIGHT) }
    var stopCurrentMode by remember { mutableStateOf(false) }

    fun nextStep() {
        if (userCanSee) correctAnswers++
        if (!userCanSee) stopCurrentMode = true

        if (!stopCurrentMode && currentStep < steps - 1) {
            currentStep++
            userCanSee = false
        } else if (currentMode == ContrastMode.DARK_ON_LIGHT) {
            currentMode = ContrastMode.LIGHT_ON_DARK
            currentStep = 0
            userCanSee = false
            stopCurrentMode = false
        } else {
            testCompleted = true
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!testCompleted) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "💡 Как проводить тест:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Держите телефон на расстоянии 50-70 см", style = MaterialTheme.typography.bodyMedium)
                        Text("• Смотрите на текст до 5 секунд, затем отмечайте, видите ли его", style = MaterialTheme.typography.bodyMedium)
                        Text("• Не щурьтесь и держите телефон прямо", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Шаг ${currentStep + 1} из $steps (${if (currentMode == ContrastMode.DARK_ON_LIGHT) "Черное на белом" else "Белое на черном"})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                val textColor = if (currentMode == ContrastMode.DARK_ON_LIGHT) Color.Black.copy(alpha = contrasts[currentStep]) else Color.White.copy(alpha = contrasts[currentStep])
                val backgroundColor = if (currentMode == ContrastMode.DARK_ON_LIGHT) Color.White else Color.Black

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 16.dp)
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Видите текст?",
                        color = textColor,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            userCanSee = true
                            nextStep()
                        }
                    ) {
                        Text("Вижу")
                    }

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            userCanSee = false
                            nextStep()
                        }
                    ) {
                        Text("Не вижу")
                    }
                }

            } else {
                // Финальный экран
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎉 Тест завершен!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Вы смогли различить контраст на $correctAnswers из ${steps * 2} шагов",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = evaluateContrastScore(correctAnswers, steps * 2),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Button(
                onClick = { navigator.pop() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Назад")
            }
        }
    }
}

fun evaluateContrastScore(correct: Int, total: Int): String {
    val percentage = (correct.toDouble() / total.toDouble()) * 100
    return when {
        percentage >= 90 -> "Отличная чувствительность к контрасту"
        percentage >= 70 -> "Хорошая чувствительность"
        percentage >= 50 -> "Средняя чувствительность"
        else -> "Низкая чувствительность, рекомендуем проконсультироваться со специалистом"
    }
}

enum class ContrastMode { DARK_ON_LIGHT, LIGHT_ON_DARK }
