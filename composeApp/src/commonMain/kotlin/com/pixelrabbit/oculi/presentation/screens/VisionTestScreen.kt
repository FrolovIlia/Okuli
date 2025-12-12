package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object VisionTestScreen : Screen {
    @Composable
    override fun Content() {
        VisionTestContent()
    }
}

data class VisionTest(
    val title: String,
    val description: String,
    val duration: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisionTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    val testTypes = listOf(
        VisionTest("👁️ Острота зрения", "Проверка по таблице Сивцева", "2-3 минуты"),
        VisionTest("🔴 Астигматизм", "Тест на искажение зрения", "1-2 минуты"),
        VisionTest("🎨 Цветовосприятие", "Проверка цветового зрения", "2 минуты")
    )

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Проверки зрения",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Помогают отслеживать здоровье ваших глаз",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            items(testTypes) { test ->
                TestTypeCard(
                    title = test.title,
                    description = test.description,
                    duration = test.duration,
                    onClick = {
                        when (test.title) {
                            "👁️ Острота зрения" -> navigator.push(AcuityTestScreen)
                            "🔴 Астигматизм" -> navigator.push(AstigmatismTestScreen)
                            "🎨 Цветовосприятие" -> navigator.push(ColorTestScreen)
                        }
                    }
                )
            }

            item {
                Button(
                    onClick = { navigator.pop() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Назад")
                }
            }
        }
    }
}

@Composable
fun TestTypeCard(
    title: String,
    description: String,
    duration: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "⏱ $duration",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
