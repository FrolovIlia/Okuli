package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import backy.composeapp.generated.resources.Res
import backy.composeapp.generated.resources.spine_flexibility
import androidx.compose.foundation.layout.FlowRow

object ForwardBendTestScreen : Screen {
    @Composable
    override fun Content() = ForwardBendTestContent()
}

private enum class BendState { IDLE, RESULT }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ForwardBendTestContent() {

    val navigator = LocalNavigator.currentOrThrow

    var distanceCm by rememberSaveable { mutableIntStateOf(0) }
    var state by rememberSaveable { mutableStateOf(BendState.IDLE) }

    val options = listOf(0, 5, 10, 15, 20, 25, 30)

    Scaffold { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "📏 Наклон вперёд",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(Res.drawable.spine_flexibility),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Как выполнять",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Наклонитесь вперёд и оцените расстояние от пальцев до пола."
                    )
                }
            }

            if (state == BendState.IDLE) {

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            "Выберите расстояние",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            options.forEach { value ->

                                val selected = distanceCm == value

                                Button(
                                    onClick = { distanceCm = value },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor =
                                            if (selected)
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor =
                                            if (selected)
                                                MaterialTheme.colorScheme.onPrimary
                                            else
                                                MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                ) {
                                    Text("$value см", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { state = BendState.RESULT },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = distanceCm > 0
                ) {
                    Text("Узнать результат")
                }
            }

            if (state == BendState.RESULT) {

                val resultText = when {
                    distanceCm <= 0 ->
                        "Отличная гибкость. Полное касание пола без ограничений."

                    distanceCm <= 3 ->
                        "Очень высокая гибкость. Задняя цепь мышц в отличном состоянии."

                    distanceCm <= 7 ->
                        "Хорошая гибкость. Минимальные ограничения."

                    distanceCm <= 12 ->
                        "Умеренная гибкость. Есть напряжение задней поверхности бедра."

                    distanceCm <= 20 ->
                        "Сниженная гибкость. Выраженное укорочение задней цепи."

                    else ->
                        "Низкая гибкость. Существенная скованность и дефицит растяжки."
                }

                val recommendations = listOf(
                    "Растяжка задней поверхности бедра ежедневно",
                    "Наклоны вперёд без рывков",
                    "Укрепление мышц кора",
                    "Избегать длительного сидения"
                )

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            "Результат: $distanceCm см",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        HorizontalDivider()

                        Text(resultText)

                        HorizontalDivider()

                        Text(
                            "Рекомендации",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        recommendations.forEach {
                            Text("• $it")
                        }
                    }
                }

                Button(
                    onClick = { navigator.pop() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Назад")
                }
            }
        }
    }
}