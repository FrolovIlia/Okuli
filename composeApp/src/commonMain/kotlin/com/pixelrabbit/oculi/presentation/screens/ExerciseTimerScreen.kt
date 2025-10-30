package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.di.ServiceLocator
import kotlinx.coroutines.delay
import com.pixelrabbit.oculi.presentation.components.ExerciseVisualization


data class ExerciseTimerScreen(
    val exerciseId: String
) : Screen {
    @Composable
    override fun Content() {
        ExerciseTimerContent(exerciseId = exerciseId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseTimerContent(exerciseId: String) {
    val navigator = LocalNavigator.currentOrThrow

    var exercise by remember { mutableStateOf<com.pixelrabbit.oculi.domain.models.Exercise?>(null) }
    var timeLeft by remember { mutableStateOf(0) }
    var totalTime by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(exerciseId) {
        val allExercises = ServiceLocator.getExercisesUseCase()
        exercise = allExercises.find { it.id == exerciseId }
        totalTime = exercise?.duration ?: 120
        timeLeft = totalTime
    }

    // Таймер
    LaunchedEffect(isRunning, timeLeft) {
        if (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
        } else if (timeLeft == 0 && isRunning) {
            isRunning = false
            isCompleted = true
            // Сохраняем результат
            exercise?.let {
                ServiceLocator.startExerciseUseCase(it.id, totalTime)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Таймер упражнения") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (exercise != null) {
                ExerciseTimerContent(
                    exercise = exercise!!,
                    timeLeft = timeLeft,
                    totalTime = totalTime,
                    isRunning = isRunning,
                    isCompleted = isCompleted,
                    onStart = { isRunning = true },
                    onPause = { isRunning = false },
                    onReset = {
                        timeLeft = totalTime
                        isRunning = false
                        isCompleted = false
                    },
                    onComplete = {
                        // Переход на экран завершения
                        navigator.push(
                            ExerciseCompleteScreen(
                                exerciseName = exercise!!.title,
                                duration = totalTime
                            )
                        )
                    }
                )
            } else {
                Text("Упражнение не найдено")
            }
        }
    }
}

@Composable
fun ExerciseTimerContent(
    exercise: com.pixelrabbit.oculi.domain.models.Exercise,
    timeLeft: Int,
    totalTime: Int,
    isRunning: Boolean,
    isCompleted: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onComplete: () -> Unit
) {
    val progress = if (totalTime > 0) {
        1f - (timeLeft.toFloat() / totalTime.toFloat())
    } else {
        0f
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Заголовок упражнения
        Text(
            text = "${exercise.icon} ${exercise.title}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Визуализация упражнения
        ExerciseVisualization(
            exerciseId = exercise.id,
            isRunning = isRunning
        )

        // Круговой прогресс
        BoxWithProgress(
            progress = progress,
            timeLeft = timeLeft,
            modifier = Modifier.padding(16.dp)
        )

        // Линейный прогресс
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Прогресс: ${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки управления
        ExerciseTimerControls(
            isRunning = isRunning,
            isCompleted = isCompleted,
            onStart = onStart,
            onPause = onPause,
            onReset = onReset,
            onComplete = onComplete
        )

        // Инструкция
        if (isRunning) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Инструкция:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = exercise.instructions.firstOrNull() ?: "Выполняйте упражнение",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun BoxWithProgress(
    progress: Float,
    timeLeft: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(200.dp),
            strokeWidth = 8.dp
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatTime(timeLeft),
                style = MaterialTheme.typography.displayLarge,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "осталось",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ExerciseTimerControls(
    isRunning: Boolean,
    isCompleted: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onComplete: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isCompleted) {
            Button(
                onClick = onComplete,
                modifier = Modifier.weight(1f)
            ) {
                Text("Завершить")
            }
        } else {
            if (isRunning) {
                Button(
                    onClick = onPause,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Пауза")
                }
            } else {
                Button(
                    onClick = onStart,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Старт")
                }
            }

            Button(
                onClick = onReset,
                modifier = Modifier.weight(1f)
            ) {
                Text("Сброс")
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}"
}