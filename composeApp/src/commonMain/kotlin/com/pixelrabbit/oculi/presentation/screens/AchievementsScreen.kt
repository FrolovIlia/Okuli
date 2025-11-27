package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.domain.models.Achievement
import kotlinx.coroutines.launch

object AchievementsScreen : Screen {
    @Composable
    override fun Content() {
        AchievementsContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AchievementsContent() {
    val navigator = LocalNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    var achievements by remember {
        mutableStateOf(emptyList<Achievement>())
    }

    var isLoading by remember { mutableStateOf(true) }

    // Используем Flow для реального времени обновления достижений
    val achievementsFlow = ServiceLocator.getAchievementsUseCase.getAchievementsFlow()

    LaunchedEffect(Unit) {
        println("🎯 ACHIEVEMENTS SCREEN: Initializing...")

        // Инициализируем начальные данные
        achievements = ServiceLocator.getAchievementsUseCase()
        isLoading = false

        println("🎯 ACHIEVEMENTS SCREEN: Initial data loaded - ${achievements.size} achievements")

        // Подписываемся на обновления через Flow
        achievementsFlow.collect { newAchievements ->
            val unlockedCount = newAchievements.count { it.unlockedAt != null }
            println("🎯 ACHIEVEMENTS SCREEN: Flow update - ${newAchievements.size} achievements, $unlockedCount unlocked")
            achievements = newAchievements
        }
    }

    // 🔥 ДОБАВЛЕНО: При открытии экрана проверяем актуальность ачивок
    LaunchedEffect(Unit) {
        // Даем время на загрузку初始数据，然后检查成就
        kotlinx.coroutines.delay(500)
        println("🎯 ACHIEVEMENTS SCREEN: Checking for new achievements...")
        ServiceLocator.achievementRepository().checkAndUnlockAchievements()
    }

    val unlockedCount = achievements.count { it.unlockedAt != null }
    val totalCount = achievements.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Достижения") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                // Показываем индикатор загрузки
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Загрузка достижений...")
                }
            } else {
                // Статистика достижений
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏆 Ваши достижения",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AchievementStat(
                                value = "$unlockedCount/$totalCount",
                                label = "Разблокировано"
                            )

                            AchievementStat(
                                value = "${(unlockedCount.toFloat() / totalCount.coerceAtLeast(1) * 100).toInt()}%",
                                label = "Прогресс"
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { unlockedCount.toFloat() / totalCount.coerceAtLeast(1) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                    }
                }

                // Список достижений
                if (achievements.isEmpty()) {
                    // Показываем сообщение если ачивок нет
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Достижения не найдены",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                println("🎯 ACHIEVEMENTS SCREEN: Retrying achievements load")
                                coroutineScope.launch {
                                    ServiceLocator.achievementRepository()
                                        .checkAndUnlockAchievements()
                                }
                            }
                        ) {
                            Text("Попробовать снова")
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(achievements) { achievement ->
                            AchievementItem(achievement = achievement)
                        }
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
}

@Composable
fun AchievementStat(value: String, label: String) {
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
fun AchievementItem(achievement: Achievement) {
    val isUnlocked = achievement.unlockedAt != null

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Иконка достижения
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = achievement.icon,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Icon(
                        imageVector = if (isUnlocked) Icons.Default.Done else Icons.Default.Lock,
                        contentDescription = if (isUnlocked) "Разблокировано" else "Заблокировано",
                        modifier = Modifier.size(16.dp),
                        tint = if (isUnlocked) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Информация о достижении
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = achievement.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = achievement.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Прогресс
                    if (!isUnlocked) {
                        LinearProgressIndicator(
                            progress = { achievement.progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${achievement.currentValue}/${achievement.targetValue}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "Выполнено",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Статус достижения
            Text(
                text = if (isUnlocked) {
                    "🎉 Получено: только что"
                } else "🔒 Заблокировано",
                style = MaterialTheme.typography.labelMedium,
                color = if (isUnlocked) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}