package com.pixelrabbit.backy.presentation.screens

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.backy.data.managers.ExerciseCompletionManager
import com.pixelrabbit.backy.di.ServiceLocator
import com.pixelrabbit.backy.domain.models.Achievement
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import backy.composeapp.generated.resources.Res
import backy.composeapp.generated.resources.*
import com.pixelrabbit.backy.utils.playBeep
import com.pixelrabbit.backy.presentation.components.CelebrationDialog
import kotlinx.datetime.Instant
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

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

    var exercise by remember { mutableStateOf<com.pixelrabbit.backy.domain.models.Exercise?>(null) }

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

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailContent(
    exercise: com.pixelrabbit.backy.domain.models.Exercise,
    navigator: cafe.adriel.voyager.navigator.Navigator
) {
    val coroutineScope = rememberCoroutineScope()

    var showCelebration by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(exercise.duration) }
    var isRunning by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }

    // Состояние для нового достижения
    var newlyUnlockedAchievement by remember { mutableStateOf<Achievement?>(null) }

    // Запоминаем время последнего разблокированного достижения
    var lastUnlockTime by rememberSaveable { mutableStateOf<Long?>(null) }

    // Подписываемся на изменения достижений
    LaunchedEffect(Unit) {
        ServiceLocator.getAchievementsUseCase.getAchievementsFlow()
            .distinctUntilChanged()
            .collect { achievements ->
                val latestUnlocked = achievements
                    .filter { it.unlockedAt != null }
                    .maxByOrNull { it.unlockedAt ?: Instant.fromEpochMilliseconds(0) }

                if (latestUnlocked != null) {
                    val isShown = ServiceLocator.achievementRepository()
                        .isAchievementShown(latestUnlocked.id)

                    if (!isShown) {
                        println("🎯 Новое достижение (первый показ): ${latestUnlocked.title}")
                        newlyUnlockedAchievement = latestUnlocked
                        ServiceLocator.achievementRepository()
                            .markAchievementAsShown(latestUnlocked.id)
                    }
                }
            }
    }

    // Функция для обработки завершения упражнения
    fun completeExercise() {
        coroutineScope.launch {
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

            // Убеждаемся, что достижения существуют
            val achievements = ServiceLocator.getAchievementsUseCase()
            println("📊 Найдено достижений: ${achievements.size}")

            // Проверяем достижения
            ServiceLocator.achievementRepository().checkAndUnlockAchievements()

            // Даем время на обработку
            delay(300)

            isCompleted = true
            showCelebration = true
            playBeep()
        }
    }

    // Запуск таймера
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0 && isRunning) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft == 0 && isRunning) {
                isRunning = false
                completeExercise()
            }
        }
    }

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

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
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
                    "neck_tilts" -> painterResource(Res.drawable.neck_tilts)
                    "neck_rotation" -> painterResource(Res.drawable.neck_rotation)
                    "shoulder_rolls" -> painterResource(Res.drawable.shoulder_rolls)
                    "shoulder_blades" -> painterResource(Res.drawable.shoulder_blades)
                    "chin_tuck" -> painterResource(Res.drawable.chin_tuck)
                    "cat_cow" -> painterResource(Res.drawable.cat_cow)
                    "side_stretch" -> painterResource(Res.drawable.side_stretch)
                    "desk_shoulder_open" -> painterResource(Res.drawable.desk_shoulder_open)
                    "thoracic_twist" -> painterResource(Res.drawable.thoracic_twist)
                    "trap_stretch" -> painterResource(Res.drawable.trap_stretch)
                    else -> null
                }

                painter?.let {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
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
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
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
                                    newlyUnlockedAchievement = null
                                    // НЕ сбрасываем lastUnlockTime!
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
                                    newlyUnlockedAchievement = null
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
                },
                newAchievement = newlyUnlockedAchievement
            )
        }
    }
}

private fun formatTimeDetail(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}"
}