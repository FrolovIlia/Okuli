package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object AstigmatismTestScreen : Screen {
    @Composable
    override fun Content() {
        AstigmatismTestContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstigmatismTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    var testStarted by remember { mutableStateOf(false) }
    var currentTest by remember { mutableStateOf(0) }
    var testCompleted by remember { mutableStateOf(false) }

    val testDescriptions = listOf(
        "Посмотрите на звезду. Все лучи должны быть одинаково четкими и прямыми. При астигматизме некоторые лучи могут казаться размытыми или изогнутыми.",
        "Осмотрите радиальные линии. Все линии должны быть одинаковой толщины и четкости. Обратите внимание, не кажутся ли некоторые направления более размытыми.",
        "Проверьте параллельные линии. Они должны быть ровными и одинаково видны. При астигматизме некоторые линии могут казаться волнистыми.",
        "Изучите концентрические круги. Все круги должны быть ровными, а линии четкими без размытия в определенных направлениях."
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .systemBarsPadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Верхний отступ
            Spacer(modifier = Modifier.height(8.dp))

            if (!testStarted) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "👁️ Тест на астигматизм",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "💡 Как проводить тест:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "• Держите телефон на расстоянии вытянутой руки",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Поочередно закройте левый и правый глаз",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Смотрите на каждое изображение по 10-15 секунд",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Обращайте внимание на искажения линий",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "• Проводите тест при хорошем освещении",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "При астигматизме некоторые линии могут казаться размытыми, изогнутыми или более темными, чем другие.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Start, // ИЗМЕНИЛ на Start вместо Center
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Кнопка начала теста
                Button(
                    onClick = { testStarted = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Начать тест")
                }

            } else if (!testCompleted) {
                // ПРОЦЕСС ТЕСТИРОВАНИЯ

                // Прогресс теста
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Прогресс теста:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "${currentTest + 1}/4",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (currentTest + 1).toFloat() / 4 },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp),
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }

                // Изображение для теста
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when(currentTest) {
                                0 -> SiemensStar(Modifier.size(250.dp))
                                1 -> RadialLines(Modifier.size(250.dp))
                                2 -> ParallelLines(Modifier.size(250.dp))
                                3 -> ConcentricCircles(Modifier.size(250.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = testDescriptions[currentTest],
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Смотрите на изображение 10-15 секунд для каждого глаза",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Кнопки управления тестом
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = {
                            if (currentTest < 3) {
                                currentTest++
                            } else {
                                testCompleted = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(
                            text = if (currentTest < 3) "Следующее изображение" else "Завершить тест",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = { testCompleted = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text("Завершить досрочно")
                    }
                }

            } else {
                // РЕЗУЛЬТАТЫ ТЕСТА
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎉 Тест завершен!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Если вы заметили:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "• Искаженные или изогнутые линии",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "• Размытие некоторых направлений",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "• Разную четкость линий",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "• Волнистость прямых линий",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Рекомендуется проконсультироваться с офтальмологом для точной диагностики",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Кнопка назад
            Button(
                onClick = {
                    if (testStarted && !testCompleted) {
                        testStarted = false
                        currentTest = 0
                    } else {
                        navigator.pop()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Назад")
            }

            // Нижний отступ
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SiemensStar(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        // Рисуем звезду Сименса (36 лучей)
        for (i in 0 until 36) {
            val angle = i * 10f
            rotate(angle, center) {
                drawLine(
                    color = Color.Black,
                    start = center,
                    end = Offset(center.x + radius, center.y),
                    strokeWidth = 2f
                )
            }
        }

        // Центральный круг
        drawCircle(
            color = Color.Black,
            center = center,
            radius = radius * 0.1f
        )
    }
}

@Composable
fun RadialLines(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        // Радиальные линии (24 линии)
        for (i in 0 until 24) {
            val angle = i * 15f
            rotate(angle, center) {
                drawLine(
                    color = Color.Black,
                    start = center,
                    end = Offset(center.x + radius, center.y),
                    strokeWidth = 2f
                )
            }
        }
    }
}

@Composable
fun ParallelLines(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        // Вертикальные параллельные линии
        for (i in 0 until 8) {
            val x = size.width * (i + 1) / 9
            drawLine(
                color = Color.Black,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 3f
            )
        }

        // Горизонтальные параллельные линии
        for (i in 0 until 8) {
            val y = size.height * (i + 1) / 9
            drawLine(
                color = Color.Black,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 3f
            )
        }
    }
}

@Composable
fun ConcentricCircles(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 2

        // Концентрические круги (5 кругов)
        for (i in 1..5) {
            val radius = maxRadius * i / 6
            drawCircle(
                color = Color.Black,
                center = center,
                radius = radius,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )
        }

        // Радиальные линии через центр (8 линий)
        for (i in 0 until 8) {
            val angle = i * 45f
            rotate(angle, center) {
                drawLine(
                    color = Color.Black,
                    start = center,
                    end = Offset(center.x + maxRadius, center.y),
                    strokeWidth = 2f
                )
            }
        }
    }
}