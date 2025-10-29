package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.di.ServiceLocator

object ExercisesListScreen : Screen {
    @Composable
    override fun Content() {
        ExercisesListContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesListContent() {
    val navigator = LocalNavigator.currentOrThrow

    var exercises by remember { mutableStateOf(emptyList<com.pixelrabbit.oculi.domain.models.Exercise>()) }

    LaunchedEffect(Unit) {
        exercises = ServiceLocator.getExercisesUseCase()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Упражнения для глаз") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(exercises) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onClick = {
                            navigator.push(ExerciseDetailScreen(exercise.id))
                        }
                    )
                }
            }

            Button(
                onClick = { navigator.pop() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Назад")
            }
        }
    }
}

@Composable
fun ExerciseCard(
    exercise: com.pixelrabbit.oculi.domain.models.Exercise,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${exercise.icon} ${exercise.title}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "⏱ ${exercise.duration / 60} мин • ${exercise.difficulty}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            if (exercise.isPremium) {
                Text(
                    text = "💎 ПРЕМИУМ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}