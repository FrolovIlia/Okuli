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
import backy.composeapp.generated.resources.neck_rotation
import androidx.compose.foundation.layout.FlowRow

object NeckRotationTestScreen : Screen {
    @Composable
    override fun Content() = NeckRotationTestContent()
}

private enum class NeckState { IDLE, RESULT }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NeckRotationTestContent() {

    val navigator = LocalNavigator.currentOrThrow

    var degree by rememberSaveable { mutableIntStateOf(0) }
    var state by rememberSaveable { mutableStateOf(NeckState.IDLE) }

    val options = listOf(20, 30, 40, 50, 60, 70, 80, 90)

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
                "🔄 Поворот шеи",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(Res.drawable.neck_rotation),
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
                        "Поверните голову до комфортного предела и выберите угол."
                    )
                }
            }

            if (state == NeckState.IDLE) {

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
                            "Выберите угол поворота",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            options.forEach { value ->

                                val selected = degree == value

                                Button(
                                    onClick = { degree = value },
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
                                    Text("$value°", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { state = NeckState.RESULT },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = degree > 0
                ) {
                    Text("Узнать результат")
                }
            }

            if (state == NeckState.RESULT) {

                val resultText = when {
                    degree >= 80 ->
                        "Отличная подвижность шеи. Полный функциональный диапазон."

                    degree >= 70 ->
                        "Очень хорошая подвижность. Минимальные ограничения."

                    degree >= 60 ->
                        "Хорошая подвижность. Лёгкие мышечные зажимы возможны."

                    degree >= 45 ->
                        "Умеренная подвижность. Признаки напряжения шейного отдела."

                    degree >= 30 ->
                        "Сниженная подвижность. Выраженные мышечные ограничения."

                    else ->
                        "Сильная скованность шеи. Требуется работа над мобильностью."
                }

                val recommendations = listOf(
                    "Мягкие вращения шеи ежедневно",
                    "Контроль положения головы при работе за экраном",
                    "Растяжка трапециевидных мышц",
                    "Избегать длительного наклона головы вперёд"
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
                            "Результат: $degree°",
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