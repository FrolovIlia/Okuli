package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeContent()
    }
}

@Composable
fun HomeContent() {
    val navigator = LocalNavigator.currentOrThrow

    var stats by remember {
        mutableStateOf(
            com.pixelrabbit.oculi.domain.use_cases.StatsResult(
                totalExercises = 0,
                totalTime = 0,
                currentStreak = 0,
                todayExercises = 0
            )
        )
    }

    LaunchedEffect(Unit) {
        stats = ServiceLocator.getStatsUseCase()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Заголовок
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "👁️ Oculi",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Тренировка зрения",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Статистика
        StatsSection(stats = stats)

        Spacer(modifier = Modifier.height(16.dp))

        // Быстрый старт
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🚀 Быстрый старт",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        navigator.push(ExercisesListScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("💪 Начать тренировку")
                }
            }
        }

        // Основные функции
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeatureButton(
                text = "👁️ Проверка зрения",
                onClick = { navigator.push(VisionTestScreen) }
            )

            FeatureButton(
                text = "📊 Мой прогресс",
                onClick = { navigator.push(ProgressScreen) }
            )

            FeatureButton(
                text = "⚙️ Настройки",
                onClick = { navigator.push(SettingsScreen) }
            )

            FeatureButton(
                text = "🏆 Достижения",
                onClick = { navigator.push(AchievementsScreen) }
            )
        }
    }
}

@Composable
fun StatsSection(stats: com.pixelrabbit.oculi.domain.use_cases.StatsResult) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📈 Ваша статистика",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                StatItem(
                    value = stats.totalExercises.toString(),
                    label = "Упражнений"
                )

                StatItem(
                    value = "${stats.totalTime} мин",
                    label = "Время"
                )

                StatItem(
                    value = "${stats.currentStreak} дн",
                    label = "Серия"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Сегодняшняя статистика
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Сегодня: ${stats.todayExercises} упражнений",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FeatureButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}