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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object SpineFlexibilityTestScreen : Screen {
    @Composable
    override fun Content() {
        SpineFlexibilityTestContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpineFlexibilityTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    var currentStep by remember { mutableIntStateOf(0) }
    var movementSucceeded by remember { mutableStateOf(false) }
    var testCompleted by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }

    val spineSteps: List<String> = listOf(
        "Наклонитесь вперед, коснитесь пальцами пола",
        "Наклонитесь назад, прогнитесь максимально",
        "Наклонитесь влево, вытягивая правую руку",
        "Наклонитесь вправо, вытягивая левую руку",
        "Повернитесь туловищем равномерно влево и вправо"
    )

    fun nextStep() {
        if (movementSucceeded) correctCount++
        if (currentStep < spineSteps.size - 1) {
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
                        Text("💡 Инструкция:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("• Держите спину ровно", style = MaterialTheme.typography.bodyMedium)
                        Text("• Выполняйте движения медленно, без рывков", style = MaterialTheme.typography.bodyMedium)
                        Text("• Остановитесь при дискомфорте", style = MaterialTheme.typography.bodyMedium)
                        Text("• Считайте движение выполненным, если достигли максимальной амплитуды", style = MaterialTheme.typography.bodyMedium)
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
                        Text("Тест подвижности спины", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Шаг ${currentStep + 1} из ${spineSteps.size}", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(16.dp))
                        Text(spineSteps[currentStep], style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(24.dp))
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(150.dp)
//                                .background(Color.Gray),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text("🎯 Placeholder для визуальной подсказки", color = Color.White, textAlign = TextAlign.Center)
//                        }
//                        Spacer(Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(modifier = Modifier.weight(1f), onClick = { movementSucceeded = true; nextStep() }) {
                                Text("Получилось")
                            }
                            Button(modifier = Modifier.weight(1f), onClick = { nextStep() }) {
                                Text("Не получается")
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
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎉 Тест завершен!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(24.dp))
                        Text("Результаты:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Text("Правильных движений: $correctCount из ${spineSteps.size}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Text("Оценка: ${calculateSpineScore(correctCount)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
                    }
                }
            }

            Button(onClick = { navigator.pop() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text("Назад")
            }
        }
    }
}

fun calculateSpineScore(correctCount: Int): String {
    val percent = correctCount.toDouble() / 5.0
    return when {
        percent >= 0.8 -> "Отличная подвижность спины"
        percent >= 0.6 -> "Хорошая подвижность"
        percent >= 0.4 -> "Средняя подвижность"
        else -> "Низкая подвижность, рекомендуется консультация специалиста"
    }
}