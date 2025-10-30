package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

object ProgressScreen : Screen {
    @Composable
    override fun Content() {
        ProgressContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressContent() {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мой прогресс") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Основная статистика
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "📊 Общая статистика",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    StatRow(
                        label = "Всего упражнений",
                        value = stats.totalExercises.toString(),
                        emoji = "💪"
                    )

                    StatRow(
                        label = "Общее время",
                        value = "${stats.totalTime} минут",
                        emoji = "⏱️"
                    )

                    StatRow(
                        label = "Текущая серия",
                        value = "${stats.currentStreak} дней",
                        emoji = "🔥"
                    )

                    StatRow(
                        label = "Сегодня",
                        value = "${stats.todayExercises} упражнений",
                        emoji = "📅"
                    )
                }
            }

            // Достижения
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🏆 Достижения",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AchievementItem(
                        title = "Первая тренировка",
                        description = "Выполните первое упражнение",
                        completed = stats.totalExercises > 0,
                        emoji = "🎯"
                    )

                    AchievementItem(
                        title = "Неделя заботы",
                        description = "Тренируйтесь 7 дней подряд",
                        completed = stats.currentStreak >= 7,
                        emoji = "📅"
                    )

                    AchievementItem(
                        title = "Мастер глаз",
                        description = "Выполните 50 упражнений",
                        completed = stats.totalExercises >= 50,
                        emoji = "👑"
                    )
                }
            }

            // Советы
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "💡 Советы",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TipItem(
                        text = "Тренируйтесь ежедневно для лучших результатов"
                    )

                    TipItem(
                        text = "Делайте перерывы каждые 20 минут работы за компьютером"
                    )

                    TipItem(
                        text = "Следите за правильным освещением во время тренировок"
                    )
                }
            }

            Button(
                onClick = { navigator.pop() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Назад")
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String, emoji: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = emoji, modifier = Modifier.padding(end = 8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AchievementItem(title: String, description: String, completed: Boolean, emoji: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (completed) "✅" else "⏳",
            modifier = Modifier.padding(end = 12.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (completed) FontWeight.Bold else FontWeight.Normal,
                color = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(text = emoji)
    }
}

@Composable
fun TipItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "•", modifier = Modifier.padding(end = 12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}