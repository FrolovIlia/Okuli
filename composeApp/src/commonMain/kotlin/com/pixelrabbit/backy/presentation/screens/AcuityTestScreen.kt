package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

enum class Eye { LEFT, RIGHT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcuityTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    var currentEye by remember { mutableStateOf(Eye.LEFT) }
    var currentLine by remember { mutableStateOf(0) }
    var currentLetter by remember { mutableStateOf(0) }
    var testCompleted by remember { mutableStateOf(false) }
    var showEyeSwitchDialog by remember { mutableStateOf(false) }

    var leftEyeResult by remember { mutableStateOf<Int?>(null) }
    var rightEyeResult by remember { mutableStateOf<Int?>(null) }

    val testLines = listOf(
        listOf("Ш", "Б"),
        listOf("М", "Н", "К"),
        listOf("Ы", "М", "Б", "Ш"),
        listOf("Б", "Ы", "Н", "К", "М"),
        listOf("И", "Н", "Ш", "К", "Ы", "Б"),
        listOf("Ш", "И", "Н", "К", "Ы", "Б", "М"),
        listOf("Б", "Ы", "Ш", "И", "К", "М", "Н"),
        listOf("И", "М", "Ш", "Ы", "Н", "Б", "К"),
        listOf("М", "Н", "К", "Ш", "Ы", "Б", "И"),
        listOf("Ш", "Б", "М", "Н", "К", "Ы", "И"),
        listOf("Б", "Ы", "М", "Ш", "И", "Н", "К"),
        listOf("К", "Ш", "М", "Ы", "И", "Б", "Н")
    )

    val fontSizeForLine = listOf(
        70.sp, 35.sp, 23.sp, 17.sp, 14.sp,
        11.sp, 9.sp, 7.sp, 6.sp, 5.sp,
        4.sp, 3.sp
    )

    val fixedLetterContainerHeight = 180.dp

    fun switchToNextEye() {
        val nextEye = when {
            leftEyeResult == null -> Eye.LEFT
            rightEyeResult == null -> Eye.RIGHT
            else -> null
        }

        if (nextEye != null) {
            currentEye = nextEye
            currentLine = 0
            currentLetter = 0
        } else {
            testCompleted = true
        }
    }

    fun handleCorrectAnswer() {
        if (currentLetter < testLines[currentLine].size - 1) {
            currentLetter++
        } else {
            currentLetter = 0
            if (currentLine < testLines.size - 1) {
                currentLine++
            } else {
                when (currentEye) {
                    Eye.LEFT -> leftEyeResult = testLines.size
                    Eye.RIGHT -> rightEyeResult = testLines.size
                }
                switchToNextEye()
            }
        }
    }

    fun handleIncorrectAnswer() {
        val passedLines = currentLine
        when (currentEye) {
            Eye.LEFT -> leftEyeResult = passedLines
            Eye.RIGHT -> rightEyeResult = passedLines
        }
        switchToNextEye()
    }

    fun manuallySwitchEye() {
        currentEye = when (currentEye) {
            Eye.LEFT -> Eye.RIGHT
            Eye.RIGHT -> Eye.LEFT
        }
        currentLine = 0
        currentLetter = 0
        showEyeSwitchDialog = false
    }

    fun forceCompleteTest() {
        val currentProgress = currentLine
        when (currentEye) {
            Eye.LEFT -> if (leftEyeResult == null) leftEyeResult = currentProgress
            Eye.RIGHT -> if (rightEyeResult == null) rightEyeResult = currentProgress
        }
        testCompleted = true
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            if (!testCompleted) {
                // Инструкция
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "💡 Как проводить тест:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Держите телефон на расстоянии вытянутой руки", style = MaterialTheme.typography.bodyMedium)
                        Text("• Закройте ${if (currentEye == Eye.LEFT) "правый" else "левый"} глаз ладонью", style = MaterialTheme.typography.bodyMedium)
                        Text("• Большими пальцами нажимайте кнопки внизу", style = MaterialTheme.typography.bodyMedium)
                        Text("• Не щурьтесь и не наклоняйте голову", style = MaterialTheme.typography.bodyMedium)
                        Text("• Проводите тест при хорошем освещении", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Текущий глаз
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = { showEyeSwitchDialog = true },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("👁️ Сейчас проверяем:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(if (currentEye == Eye.LEFT) "ЛЕВЫЙ ГЛАЗ" else "ПРАВЫЙ ГЛАЗ", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        val leftProgress = leftEyeResult ?: if (currentEye == Eye.LEFT) currentLine else 0
                        val rightProgress = rightEyeResult ?: if (currentEye == Eye.RIGHT) currentLine else 0
                        Text("Левый: $leftProgress/${testLines.size} | Правый: $rightProgress/${testLines.size}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("↕️ Нажмите чтобы сменить глаз", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Прогресс линии
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Прогресс ${if (currentEye == Eye.LEFT) "левого" else "правого"} глаза:", style = MaterialTheme.typography.titleMedium)
                            Text("${currentLine + 1}/${testLines.size}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = (currentLine + 1).toFloat() / testLines.size,
                            modifier = Modifier.fillMaxWidth().height(12.dp),
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Строка ${currentLine + 1} из ${testLines.size}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                // Буква
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Какая эта буква?", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp), textAlign = TextAlign.Center)
                        Box(modifier = Modifier.height(fixedLetterContainerHeight).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Text(testLines[currentLine][currentLetter], style = MaterialTheme.typography.displayLarge, fontSize = fontSizeForLine[currentLine], fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                            Button(onClick = { handleCorrectAnswer() }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                                Text("✅ ВИЖУ", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            }
                            Button(onClick = { handleIncorrectAnswer() }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                                Text("❌ НЕ ВИЖУ", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("👆 Нажимайте большими пальцами", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                    }
                }

                Button(
                    onClick = { forceCompleteTest() },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = MaterialTheme.shapes.large
                ) { Text("Завершить тест") }

            } else {
                // Результаты
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🎉 Тест завершен!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            ResultItem("Левый глаз", leftEyeResult ?: 0, testLines.size)
                            ResultItem("Правый глаз", rightEyeResult ?: 0, testLines.size)
                        }
                    }
                }
            }

            Button(
                onClick = { navigator.pop() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.large
            ) { Text("Назад") }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showEyeSwitchDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showEyeSwitchDialog = false },
            title = { Text("Смена глаза") },
            text = { Text("Вы хотите переключиться на проверку другого глаза? Текущий прогресс будет сохранен.") },
            confirmButton = {
                Button(onClick = { manuallySwitchEye() }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                    Text("Переключить на ${if (currentEye == Eye.LEFT) "правый" else "левый"} глаз")
                }
            },
            dismissButton = {
                Button(onClick = { showEyeSwitchDialog = false }, modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                    Text("Продолжить текущий глаз")
                }
            }
        )
    }
}

@Composable
fun ResultItem(eye: String, linesPassed: Int, totalLines: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(eye, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Пройдено строк: $linesPassed из $totalLines", style = MaterialTheme.typography.bodyMedium)
            Text("Острота зрения: ${formatAcuityValue(calculateAcuityForLines(linesPassed))}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

fun calculateAcuityForLines(linesPassed: Int): Double {
    return when (linesPassed) {
        12 -> 2.0
        11 -> 1.5
        10 -> 1.2
        9 -> 1.0
        8 -> 0.9
        7 -> 0.8
        6 -> 0.7
        5 -> 0.6
        4 -> 0.5
        3 -> 0.4
        2 -> 0.3
        1 -> 0.2
        0 -> 0.1
        else -> 0.1
    }
}

fun formatAcuityValue(acuity: Double): String {
    val formatted = if (acuity >= 1.0) "${acuity.toInt()}.0" else acuity.toString()
    return if (formatted.contains('.')) {
        val parts = formatted.split('.')
        if (parts[1].length > 1) "${parts[0]}.${parts[1].substring(0, 1)}" else formatted
    } else "$formatted.0"
}
