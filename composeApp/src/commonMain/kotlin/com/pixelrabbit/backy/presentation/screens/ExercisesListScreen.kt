package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.backy.di.ServiceLocator
import com.pixelrabbit.backy.domain.models.Exercise
import kotlinx.coroutines.launch

/* ---------------- SCREEN ---------------- */

data object ExercisesListScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel {
            ExercisesListScreenModel()
        }
        ExercisesListContent(screenModel)
    }
}

/* ---------------- SCREEN MODEL ---------------- */

class ExercisesListScreenModel : ScreenModel {

    val listState = LazyListState()

    var exercises by mutableStateOf<List<Exercise>>(emptyList())
        private set

    init {
        screenModelScope.launch {
            exercises = ServiceLocator.getExercisesUseCase()
        }
    }
}

/* ---------------- UI ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesListContent(
    screenModel: ExercisesListScreenModel
) {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = screenModel.listState,
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Упражнения для глаз",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Помогают поддерживать и улучшать зрение",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            items(
                items = screenModel.exercises,
                key = { it.id }
            ) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onClick = {
                        navigator.push(
                            ExerciseDetailScreen(exercise.id)
                        )
                    }
                )
            }

            item {
                Button(
                    onClick = { navigator.pop() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Назад")
                }
            }
        }
    }
}

/* ---------------- CARD ---------------- */

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
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
        }
    }
}
