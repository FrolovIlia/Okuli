package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object AcuityTestScreen : Screen {
    @Composable
    override fun Content() {
        AcuityTestContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcuityTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    var currentLine by remember { mutableStateOf(0) }
    var currentLetter by remember { mutableStateOf(0) }
    var testCompleted by remember { mutableStateOf(false) }
    var distance by remember { mutableStateOf(2f) } // в метрах

    val testLines = listOf(
        listOf("Ш", "Б", "М", "Н", "К", "Ы", "И"),
        listOf("М", "Н", "К", "Ш", "Ы", "Б", "И"),
        listOf("И", "М", "Ш", "Ы", "Н", "Б", "К"),
        listOf("Б", "Ы", "Ш", "И", "К", "М", "Н"),
        listOf("Ш", "И", "Н", "К", "М", "Б", "Ы")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Острота зрения") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (!testCompleted) {
                // Прогресс теста
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Прогресс теста",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CircularProgressIndicator(
                        progress = { (currentLine + 1).toFloat() / testLines.size },
                        modifier = Modifier.size(60.dp),
                        strokeWidth = 4.dp
                    )

                    Text(
                        text = "${currentLine + 1} из ${testLines.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Отображение букв
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Что вы видите?",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Text(
                            text = testLines[currentLine][currentLetter],
                            style = MaterialTheme.typography.displayLarge,
                            fontSize = (120 - currentLine * 20).sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Кнопки ответа
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    // Правильный ответ
                                    if (currentLetter < testLines[currentLine].size - 1) {
                                        currentLetter++
                                    } else {
                                        currentLetter = 0
                                        if (currentLine < testLines.size - 1) {
                                            currentLine++
                                        } else {
                                            testCompleted = true
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Вижу")
                            }

                            Button(
                                onClick = {
                                    // Не видит - завершаем тест
                                    testCompleted = true
                                },
                                modifier = Modifier.weight(1f),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text("Не вижу")
                            }
                        }
                    }
                }

                // Настройка расстояния
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Расстояние до экрана: ${formatDistance(distance)} м",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = distance,
                            onValueChange = { distance = it },
                            valueRange = 1f..4f,
                            steps = 6,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "Отойдите на указанное расстояние от экрана",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Результаты теста
                Card(
                    modifier = Modifier.fillMaxWidth()
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
                            text = "Ваша острота зрения:",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = calculateAcuity(currentLine, distance),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        Text(
                            text = "Пройдено строк: ${currentLine + 1}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                // Сохранить результат и вернуться
                                navigator.pop()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Сохранить результат")
                        }
                    }
                }
            }

            Button(
                onClick = { navigator.pop() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}

fun formatDistance(distance: Float): String {
    val distanceStr = distance.toString()
    return if (distanceStr.contains('.')) {
        val parts = distanceStr.split('.')
        "${parts[0]}.${parts[1].take(1)}"
    } else {
        "$distance.0"
    }
}

fun calculateAcuity(line: Int, distance: Float): String {
    val baseAcuity = 1.0 - (line * 0.1)
    val distanceFactor = distance / 2.0 // Нормальное расстояние 2 метра
    val finalAcuity = baseAcuity * distanceFactor
    val acuityValue = finalAcuity.coerceAtLeast(0.1)

    // Форматируем до двух знаков после запятой
    val acuityStr = acuityValue.toString()
    return if (acuityStr.contains('.')) {
        val parts = acuityStr.split('.')
        "${parts[0]}.${parts[1].take(2).padEnd(2, '0')}"
    } else {
        "$acuityStr.00"
    }
}