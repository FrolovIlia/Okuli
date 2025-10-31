package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.utils.getGreeting
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


object HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeContent()
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeContent() {
    val navigator = LocalNavigator.currentOrThrow
    val density = LocalDensity.current

    // Учет отступов от системных тулбаров
    val topPadding = with(density) {
        24.dp + WindowInsets.statusBars.getTop(this).dp
    }
    val bottomPadding = with(density) {
        24.dp + WindowInsets.navigationBars.getBottom(this).dp
    }

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
            .padding(
                top = topPadding,
                bottom = bottomPadding,
                start = 24.dp,
                end = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Заголовок с логотипом
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(org.jetbrains.compose.resources.DrawableResource("drawable/ic_logo_standart.png")),
                    contentDescription = "Oculi Logo",
                    modifier = Modifier.size(50.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Oculi",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "${getGreeting()}!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Тренировка зрения",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Статистика
        StatsSection(stats = stats)

        Spacer(modifier = Modifier.height(8.dp))

        // Быстрый старт
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Быстрый старт",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navigator.push(ExercisesListScreen)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Начать тренировку")
                }
            }
        }

        // Основные функции в виде компактных кнопок
        FeatureButtonsGrid(navigator)
    }
}

@Composable
fun StatsSection(stats: com.pixelrabbit.oculi.domain.use_cases.StatsResult) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Ваша статистика",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
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

            Spacer(modifier = Modifier.height(12.dp))

            // Сегодняшняя статистика
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Сегодня: ${stats.todayExercises} упражнений",
                    style = MaterialTheme.typography.bodyMedium,
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
            style = MaterialTheme.typography.titleMedium,
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
fun FeatureButtonsGrid(navigator: Navigator) {
    val features = listOf(
        "Проверка зрения" to { navigator.push(VisionTestScreen) },
        "Мой прогресс" to { navigator.push(ProgressScreen) },
        "Достижения" to { navigator.push(AchievementsScreen) },
        "Настройки" to { navigator.push(SettingsScreen) },
        "О приложении" to { navigator.push(AboutScreen) }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(features) { (text, onClick) ->
            CompactFeatureButton(
                text = text,
                onClick = onClick
            )
        }
    }
}

@Composable
fun CompactFeatureButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
