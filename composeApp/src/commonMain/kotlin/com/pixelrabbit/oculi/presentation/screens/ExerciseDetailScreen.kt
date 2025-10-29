package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.di.ServiceLocator

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
        topBar = {
            TopAppBar(
                title = { Text("Упражнение") }
            )
        }
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

@Composable
fun ExerciseDetailContent(
    exercise: com.pixelrabbit.oculi.domain.models.Exercise,
    navigator: cafe.adriel.voyager.navigator.Navigator
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Заголовок
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
                Text(
                    text = "Тип: ${exercise.type} • Сложность: ${exercise.difficulty}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Описание
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

        // Инструкции
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

        // Кнопки действий
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    navigator.push(ExerciseTimerScreen(exercise.id))
                },
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Начать упражнение (${exercise.duration / 60} мин)")
            }

            Button(
                onClick = { navigator.pop() },
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Назад к списку")
            }
        }
    }
}