package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow


object ColorTestScreen : Screen {
    private fun readResolve(): Any = ColorTestScreen

    @Composable
    override fun Content() {
        ColorTestContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    var currentEye by remember { mutableStateOf(Eye.LEFT) }
    var currentPlate by remember { mutableStateOf(0) }
    var testCompleted by remember { mutableStateOf(false) }
    var showEyeSwitchDialog by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var answerChecked by remember { mutableStateOf(false) }

    var leftEyeCorrectAnswers by remember { mutableStateOf(0) }
    var rightEyeCorrectAnswers by remember { mutableStateOf(0) }

    val testPlates = listOf(
        ColorPlate(
            number = 1,
            correctAnswer = "12",
            description = "Цифра 12",
            options = listOf("12", "13", "—", "72")
        ),
        ColorPlate(
            number = 2,
            correctAnswer = "2",
            description = "Цифра 2",
            options = listOf("8", "2", "—", "3")
        ),
        ColorPlate(
            number = 3,
            correctAnswer = "6",
            description = "Цифра 6",
            options = listOf("9", "8", "6", "—")
        ),
        ColorPlate(
            number = 4,
            correctAnswer = "42",
            description = "Цифра 42",
            options = listOf("42", "24", "—", "45")
        ),
        ColorPlate(
            number = 5,
            correctAnswer = "74",
            description = "Цифра 74",
            options = listOf("47", "74", "14", "—")
        )
    )

    fun switchToNextEye() {
        val nextEye = when {
            currentEye == Eye.LEFT && rightEyeCorrectAnswers == 0 -> Eye.RIGHT
            else -> null
        }

        if (nextEye != null) {
            currentEye = nextEye
            currentPlate = 0
            selectedAnswer = null
            answerChecked = false
        } else {
            testCompleted = true
        }
    }

    fun handleAnswerSelect(answer: String) {
        if (!answerChecked) {
            selectedAnswer = answer
            answerChecked = true

            if (answer == testPlates[currentPlate].correctAnswer) {
                when (currentEye) {
                    Eye.LEFT -> leftEyeCorrectAnswers++
                    Eye.RIGHT -> rightEyeCorrectAnswers++
                }
            }
        }
    }

    fun moveToNextPlate() {
        if (currentPlate < testPlates.size - 1) {
            currentPlate++
            selectedAnswer = null
            answerChecked = false
        } else {
            switchToNextEye()
            selectedAnswer = null
            answerChecked = false
        }
    }

    fun manuallySwitchEye() {
        currentEye = when (currentEye) {
            Eye.LEFT -> Eye.RIGHT
            Eye.RIGHT -> Eye.LEFT
        }
        currentPlate = 0
        selectedAnswer = null
        answerChecked = false
        showEyeSwitchDialog = false
    }

    fun forceCompleteTest() {
        testCompleted = true
    }

    Scaffold { innerPadding ->
        Column(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .systemBarsPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))

            if (!testCompleted) {
                Card(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
                        Text(
                            text = "💡 Как проводить тест:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                        Text(
                            text = "• Держите телефон на расстоянии 50-70 см",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Закройте ${if (currentEye == Eye.LEFT) "правый" else "левый"} глаз ладонью",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Смотрите на каждое изображение 3-5 секунд",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Не щурьтесь и не наклоняйте голову",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Проводите тест при хорошем освещении",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Выберите цифру, которую видите на изображении",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Card(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    onClick = { showEyeSwitchDialog = true }
                ) {
                    Column(
                        modifier = androidx.compose.ui.Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "👁️ Сейчас проверяем:",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (currentEye == Eye.LEFT) "ЛЕВЫЙ ГЛАЗ" else "ПРАВЫЙ ГЛАЗ",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Левый: $leftEyeCorrectAnswers/${testPlates.size} | Правый: $rightEyeCorrectAnswers/${testPlates.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "↕️ Нажмите чтобы сменить глаз",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Card(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
                        Row(
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Прогресс ${if (currentEye == Eye.LEFT) "левого" else "правого"} глаза:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${currentPlate + 1}/${testPlates.size}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (currentPlate + 1).toFloat() / testPlates.size },
                            modifier = androidx.compose.ui.Modifier
                                .fillMaxWidth()
                                .height(12.dp),
                            trackColor = MaterialTheme.colorScheme.surface
                        )
                        Spacer(modifier = androidx.compose.ui.Modifier.height(4.dp))
                        Text(
                            text = "Пластина ${currentPlate + 1} из ${testPlates.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                ColorPlateDisplay(
                    plate = testPlates[currentPlate],
                    selectedAnswer = selectedAnswer,
                    answerChecked = answerChecked,
                    onAnswerSelect = { answer: String -> handleAnswerSelect(answer) },
                    onNextPlate = { moveToNextPlate() },
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Button(
                    onClick = { forceCompleteTest() },
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Завершить тест")
                }

            } else {
                Card(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = androidx.compose.ui.Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎉 Тест завершен!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
                        Column(
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ColorResultItem(
                                eye = "Левый глаз",
                                correctAnswers = leftEyeCorrectAnswers,
                                totalPlates = testPlates.size
                            )
                            ColorResultItem(
                                eye = "Правый глаз",
                                correctAnswers = rightEyeCorrectAnswers,
                                totalPlates = testPlates.size
                            )
                        }
                        Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
                        Card(
                            modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = androidx.compose.ui.Modifier.padding(16.dp)) {
                                Text(
                                    text = "📊 Оценка цветовосприятия:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
                                Text(
                                    text = when {
                                        leftEyeCorrectAnswers >= 4 && rightEyeCorrectAnswers >= 4 ->
                                            "✅ Нормальное цветовосприятие"
                                        leftEyeCorrectAnswers >= 3 && rightEyeCorrectAnswers >= 3 ->
                                            "⚠️ Незначительные нарушения"
                                        else ->
                                            "❌ Выраженные нарушения цветовосприятия"
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { navigator.pop() },
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Назад")
            }

            Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
        }
    }

    if (showEyeSwitchDialog) {
        AlertDialog(
            onDismissRequest = { showEyeSwitchDialog = false },
            title = { Text("Смена глаза") },
            text = { Text("Вы хотите переключиться на проверку другого глаза? Текущий прогресс будет сохранен.") },
            confirmButton = {
                Button(
                    onClick = { manuallySwitchEye() },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Переключить на ${if (currentEye == Eye.LEFT) "правый" else "левый"} глаз")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showEyeSwitchDialog = false },
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Продолжить текущий глаз")
                }
            }
        )
    }
}

@Composable
fun ColorResultItem(eye: String, correctAnswers: Int, totalPlates: Int) {
    Card(
        modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = androidx.compose.ui.Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = eye,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = androidx.compose.ui.Modifier.height(8.dp))
            Text(
                text = "Правильных ответов: $correctAnswers из $totalPlates",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Результат: ${calculateColorVisionScore(correctAnswers, totalPlates)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

fun calculateColorVisionScore(correctAnswers: Int, totalPlates: Int): String {
    val percentage = (correctAnswers.toDouble() / totalPlates.toDouble()) * 100
    return when {
        percentage >= 80 -> "Отличное цветовосприятие"
        percentage >= 60 -> "Хорошее цветовосприятие"
        percentage >= 40 -> "Удовлетворительное"
        else -> "Требуется консультация специалиста"
    }
}
