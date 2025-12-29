package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.domain.models.UserProgress
import com.pixelrabbit.oculi.utils.formatTotalTime

object ProgressScreen : Screen {
    @Composable
    override fun Content() {
        ProgressContent()
    }
}

@Composable
fun ProgressContent() {
    val navigator = LocalNavigator.currentOrThrow

    var userProgress by remember { mutableStateOf(UserProgress()) }

    val statsFlow = ServiceLocator.getStatsUseCase.getStatsFlow()

    LaunchedEffect(Unit) {
        userProgress = ServiceLocator.getStatsUseCase()
        statsFlow.collect { progress ->
            userProgress = progress
        }
    }

    // Константы для отступов
    val BlockVerticalSpacing = 24.dp
    val InnerBlockPadding = 16.dp

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(BlockVerticalSpacing)
        ) {

            // Общая статистика
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = InnerBlockPadding),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(InnerBlockPadding)) {
                    Text(
                        text = "Общая статистика",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    StatRow("Всего упражнений", userProgress.totalExercises.toString(), "💪")
                    StatRow("Общее время", formatTotalTime(userProgress.totalTime), "⏱️")
                    StatRow("Текущая серия", "${userProgress.currentStreak} дней", "🔥")
                    StatRow("Сегодня", "${userProgress.todayExercises} упражнений", "📅")
                }
            }

            // Достижения
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = InnerBlockPadding),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(InnerBlockPadding)) {
                    Text(
                        text = "Достижения",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Отслеживайте свой прогресс и получайте достижения",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { navigator.push(AchievementsScreen) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Посмотреть все достижения")
                    }
                }
            }

            // Советы
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = InnerBlockPadding),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(InnerBlockPadding)) {
                    Text(
                        text = "Советы",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TipItem("Тренируйтесь ежедневно для лучших результатов")
                    TipItem("Делайте перерывы каждые 20 минут работы за компьютером")
                    TipItem("Следите за правильным освещением во время тренировок")
                }
            }

            // Кнопка назад
            Button(
                onClick = { navigator.pop() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = InnerBlockPadding)
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
