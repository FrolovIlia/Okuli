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

object ShoulderFlexibilityTestScreen : Screen {
    @Composable
    override fun Content() {
        ShoulderFlexibilityTestContent()
    }
}

enum class ShoulderMovement { FRONT_RAISE, BACK_RAISE, ARM_CIRCLE, CROSS_OVER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoulderFlexibilityTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    var currentStep by remember { mutableStateOf(0) }
    var testCompleted by remember { mutableStateOf(false) }
    var movementSucceeded by remember { mutableStateOf(false) }
    var correctMovements by remember { mutableStateOf(0) }

    val steps = listOf(
        ShoulderMovement.FRONT_RAISE to "Поднимите руки вперед и вверх",
        ShoulderMovement.BACK_RAISE to "Поднимите руки назад и вверх",
        ShoulderMovement.ARM_CIRCLE to "Вращайте руки по кругу вперед",
        ShoulderMovement.CROSS_OVER to "Сведите руки перед грудью и разведите назад"
    )

    fun nextStep() {
        if (movementSucceeded) correctMovements++
        if (currentStep < steps.size - 1) {
            currentStep++
            movementSucceeded = false
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
                            text = "💡 Как выполнять тест гибкости плеч:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Держите спину прямо", style = MaterialTheme.typography.bodyMedium)
                        Text("• Выполняйте движения медленно и контролируемо", style = MaterialTheme.typography.bodyMedium)
                        Text("• Остановитесь при дискомфорте", style = MaterialTheme.typography.bodyMedium)
                        Text("• Считайте движение выполненным, если достигли полной амплитуды", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Шаг ${currentStep + 1} из ${steps.size}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = steps[currentStep].second,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎯 Placeholder для визуальной подсказки", color = Color.White, textAlign = TextAlign.Center)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    movementSucceeded = true
                                    nextStep()
                                }
                            ) {
                                Text("Выполнил")
                            }
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { nextStep() }
                            ) {
                                Text("Не смог")
                            }
                        }
                    }
                }

            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
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
                            text = "Вы смогли выполнить $correctMovements из ${steps.size} движений",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = when {
                                correctMovements == steps.size -> "Отличная гибкость плеч"
                                correctMovements >= steps.size * 0.7 -> "Хорошая гибкость"
                                correctMovements >= steps.size * 0.4 -> "Средняя гибкость"
                                else -> "Низкая гибкость, рекомендуется упражнения и разминка"
                            },
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