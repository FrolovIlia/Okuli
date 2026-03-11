package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.pixelrabbit.backy.di.ServiceLocator
import com.pixelrabbit.backy.domain.models.UserProgress
import com.pixelrabbit.backy.presentation.theme.ThemeController
import com.pixelrabbit.backy.utils.formatTotalTime
import com.pixelrabbit.backy.utils.getGreeting
import kotlinx.coroutines.delay
import backy.composeapp.generated.resources.Res
import backy.composeapp.generated.resources.ic_logo_standart
import org.jetbrains.compose.resources.painterResource

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val darkTheme by ThemeController.themeState.collectAsState()
        HomeContent(darkTheme)
    }
}

@Composable
fun HomeContent(darkTheme: Boolean) {
    val navigator = LocalNavigator.currentOrThrow
    val density = LocalDensity.current

    val statusBarTop = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
    val navigationBarBottom = with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }

    var userProgress by remember { mutableStateOf(UserProgress()) }

    LaunchedEffect(Unit) {
        ServiceLocator.getStatsUseCase.getStatsFlow().collect { progress ->
            userProgress = progress
        }
        delay(500)
        userProgress = ServiceLocator.getStatsUseCase()
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = statusBarTop + 24.dp, start = 24.dp, end = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp) // единый интервал между секциями
        ) {
            HeaderSection()
            StatsSection(userProgress = userProgress)
            QuickStartCard(navigator)
            FeatureButtonsGrid(navigator, navigationBarBottom)
        }
    }
}

@Composable
fun HeaderSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(Res.drawable.ic_logo_standart),
                contentDescription = "Backy Logo",
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Backy",
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
            text = "Тренировка осанки",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StatsSection(userProgress: UserProgress) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Ваша статистика",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                StatItem(value = userProgress.totalExercises.toString(), label = "Упражнений")
                StatItem(value = formatTotalTime(userProgress.totalTime), label = "Время")
                StatItem(value = "${userProgress.currentStreak} дн", label = "Серия")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Сегодня: ${userProgress.todayExercises} упражнений",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
fun QuickStartCard(navigator: Navigator) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Быстрый старт",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navigator.push(ExercisesListScreen) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Начать тренировку")
            }
        }
    }
}

@Composable
fun FeatureButtonsGrid(navigator: Navigator, navigationBarBottom: androidx.compose.ui.unit.Dp) {
    val features = listOf(
        "Проверка гибкости" to { navigator.push(FlexibilityTestListScreen) },
        "Мой прогресс" to { navigator.push(ProgressScreen) },
        "Достижения" to { navigator.push(AchievementsScreen) },
        "Настройки" to { navigator.push(SettingsScreen) },
        "О приложении" to { navigator.push(AboutScreen) }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = navigationBarBottom + 24.dp)
    ) {
        items(features) { feature ->
            CompactFeatureButton(text = feature.first, onClick = feature.second)
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
