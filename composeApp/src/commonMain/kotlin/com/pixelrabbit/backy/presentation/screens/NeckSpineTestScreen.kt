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

object NeckSpineTestScreen : Screen {
    @Composable
    override fun Content() {
        NeckSpineTestContent()
    }
}

enum class TestStage { NECK, SPINE }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeckSpineTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    var stage by remember { mutableStateOf(TestStage.NECK) }
    var currentStep by remember { mutableIntStateOf(0) }
    var movementSucceeded by remember { mutableStateOf(false) }
    var testCompleted by remember { mutableStateOf(false) }

    var neckCorrect by remember { mutableIntStateOf(0) }
    var spineCorrect by remember { mutableIntStateOf(0) }

    val neckSteps: List<String> = listOf(
        "Поверните голову влево максимально",
        "Поверните голову вправо максимально",
        "Наклоните голову вперед",
        "Наклоните голову назад"
    )

    val spineSteps: List<String> = listOf(
        "Наклонитесь вперед, коснитесь пальцами пола",
        "Наклонитесь назад, прогнитесь максимально",
        "Наклонитесь влево",
        "Наклонитесь вправо",
        "Повернитесь туловищем влево и вправо"
    )

    val steps: List<String> = if (stage == TestStage.NECK) neckSteps else spineSteps

    fun nextStep() {
        if (movementSucceeded) {
            if (stage == TestStage.NECK) neckCorrect++ else spineCorrect++
        }
        if (currentStep < steps.size - 1) {
            currentStep++
            movementSucceeded = false
        } else {
            if (stage == TestStage.NECK) {
                stage = TestStage.SPINE
                currentStep = 0
                movementSucceeded = false
            } else {
                testCompleted = true
            }
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
                        Text("• Держите спину и шею ровно", style = MaterialTheme.typography.bodyMedium)
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
                        Text(
                            text = if (stage == TestStage.NECK) "Тест шеи" else "Тест спины",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Шаг ${currentStep + 1} из ${steps.size}", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(16.dp))
                        Text(steps[currentStep], style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(24.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🎯 Placeholder для визуальной подсказки", color = Color.White, textAlign = TextAlign.Center)
                        }
                        Spacer(Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(modifier = Modifier.weight(1f), onClick = { movementSucceeded = true; nextStep() }) {
                                Text("Выполнил")
                            }
                            Button(modifier = Modifier.weight(1f), onClick = { nextStep() }) {
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
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎉 Тест завершен!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(24.dp))
                        Text("Результаты:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Text("Шея: $neckCorrect из ${neckSteps.size} движений", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text("Спина: $spineCorrect из ${spineSteps.size} движений", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Text("Оценка: ${calculateNeckSpineScore(neckCorrect, spineCorrect)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
                    }
                }
            }

            Button(onClick = { navigator.pop() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text("Назад")
            }
        }
    }
}

fun calculateNeckSpineScore(neckCorrect: Int, spineCorrect: Int): String {
    val total = neckCorrect + spineCorrect
    val max = 4 + 5
    val percent = total.toDouble() / max.toDouble()
    return when {
        percent >= 0.8 -> "Отличная подвижность шеи и спины"
        percent >= 0.6 -> "Хорошая подвижность"
        percent >= 0.4 -> "Средняя подвижность"
        else -> "Низкая подвижность, рекомендуется консультация специалиста"
    }
}