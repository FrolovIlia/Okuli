package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

// Экран со списком тестов гибкости
object FlexibilityTestListScreen : Screen {
    @Composable
    override fun Content() {
        FlexibilityTestListContent()
    }
}

// Модель данных для теста
data class FlexibilityTest(
    val title: String,
    val description: String,
    val emoji: String,
    val durationMinutes: Int,
    val screen: Screen
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexibilityTestListContent() {
    val navigator = LocalNavigator.currentOrThrow

    val tests = listOf(
        FlexibilityTest(
            title = "Гибкость шеи",
            description = "Диапазон движений шеи и вращение головы",
            emoji = "🧍‍♂️",
            durationMinutes = 2,
            screen = NeckFlexibilityTestScreen
        ),
        FlexibilityTest(
            title = "Подвижность плеч",
            description = "Подвижность плечевых суставов и верхней части спины",
            emoji = "💪",
            durationMinutes = 2,
            screen = ShoulderFlexibilityTestScreen
        ),
        FlexibilityTest(
            title = "Гибкость поясницы",
            description = "Сгибание, разгибание и вращение поясницы",
            emoji = "🧘",
            durationMinutes = 2,
            screen = SpineFlexibilityTestScreen
        ),
        FlexibilityTest(
            title = "Скручивание туловища",
            description = "Подвижность грудного отдела и боковое скручивание",
            emoji = "🤸",
            durationMinutes = 3,
            screen = NeckSpineTestScreen
        )
    )

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Заголовок
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🧘 Тесты гибкости и подвижности",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Помогают оценить диапазон движений шеи, спины и плеч",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Список тестов
            items(tests, key = { it.title }) { test ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clickable { navigator.push(test.screen) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = test.emoji,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = test.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = test.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "⏱ ${test.durationMinutes} мин",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Кнопка назад
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