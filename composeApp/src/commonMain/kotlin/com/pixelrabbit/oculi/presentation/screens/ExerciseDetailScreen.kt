package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.data.managers.ExerciseCompletionManager
import com.pixelrabbit.oculi.di.ServiceLocator
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import oculi.composeapp.generated.resources.Res
import oculi.composeapp.generated.resources.*
import com.pixelrabbit.oculi.utils.playBeep
import com.pixelrabbit.oculi.presentation.components.CelebrationDialog

data class ExerciseDetailScreen(
    val exerciseId: String
) : Screen {
    @Composable
    override fun Content() {
        ExerciseDetailContent(exerciseId = exerciseId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailContent(exerciseId: String) {
    val navigator = LocalNavigator.currentOrThrow

    var exercise by remember { mutableStateOf<com.pixelrabbit.oculi.domain.models.Exercise?>(null) }

    LaunchedEffect(exerciseId) {
        val allExercises = ServiceLocator.getExercisesUseCase()
        exercise = allExercises.find { it.id == exerciseId }
    }

    Scaffold(
        topBar = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (exercise != null) {
                ExerciseDetailContent(exercise!!, navigator)
            } else {
                Text("Упражнение не найдено", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExerciseDetailContent(
    exercise: com.pixelrabbit.oculi.domain.models.Exercise,
    navigator: cafe.adriel.voyager.navigator.Navigator
) {
    var showCelebration by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exercise.icon,
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Column {
                    Text(
                        text = exercise.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Описание",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = exercise.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Инструкция",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    exercise.instructions.forEachIndexed { index, instruction ->
                        Text(
                            text = "${index + 1}. $instruction",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            exercise.imageName?.let { name ->
                val painter = when (name) {
                    "sledovanie_za" -> painterResource(Res.drawable.sledovanie_za)
                    "focus" -> painterResource(Res.drawable.focus)
                    "palming" -> painterResource(Res.drawable.palming)
                    "vosmerka" -> painterResource(Res.drawable.vosmerka)
                    else -> null
                }

                painter?.let {
                    Card {
                        Image(
                            painter = it,
                            contentDescription = exercise.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                var timeLeft by remember { mutableStateOf(exercise.duration) }
                var isRunning by remember { mutableStateOf(false) }
                var isCompleted by remember { mutableStateOf(false) }

                LaunchedEffect(isRunning, timeLeft) {
                    if (isRunning && timeLeft > 0) {
                        delay(1000)
                        timeLeft--
                    } else if (isRunning && timeLeft == 0) {
                        isRunning = false
                        isCompleted = true
                        showCelebration = true
                        playBeep()
                        if (!isSaved) {
                            ExerciseCompletionManager.saveExerciseCompletion(
                                exerciseId = exercise.id,
                                exerciseName = exercise.title,
                                duration = exercise.duration,
                                difficulty = exercise.difficulty.name,
                                successRate = 100f
                            )
                            isSaved = true
                        }
                    }
                }

                val progress = if (exercise.duration > 0)
                    1f - (timeLeft.toFloat() / exercise.duration.toFloat())
                else 0f

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "⏱ Таймер упражнения",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Осталось: ${formatTimeDetail(timeLeft)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isCompleted) {
                            Button(
                                onClick = {
                                    isCompleted = false
                                    timeLeft = exercise.duration
                                    isSaved = false
                                    showCelebration = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Завершено ✅")
                            }
                        } else {
                            if (isRunning) {
                                Button(
                                    onClick = { isRunning = false },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Пауза")
                                }
                            } else {
                                Button(
                                    onClick = { isRunning = true },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Старт")
                                }
                            }

                            Button(
                                onClick = {
                                    timeLeft = exercise.duration
                                    isRunning = false
                                    isCompleted = false
                                    isSaved = false
                                    showCelebration = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Сброс")
                            }
                        }
                    }

                    Button(
                        onClick = { navigator.pop() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Назад к списку")
                    }
                }
            }
        }

        if (showCelebration) {
            CelebrationDialog(
                onDismiss = {
                    showCelebration = false
                    navigator.pop()
                }
            )
        }
    }
}

private fun formatTimeDetail(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}"
}