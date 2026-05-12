package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import backy.composeapp.generated.resources.Res
import backy.composeapp.generated.resources.neck_rotation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
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
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                "Поверните голову до комфортного предела и выберите угол.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (state == NeckState.IDLE) {

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    options.forEach { value ->

                        val selected = degree == value

                        Button(
                            onClick = { degree = value },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (selected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant,
                                contentColor =
                                    if (selected)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text("$value°", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Button(
                    onClick = { state = NeckState.RESULT },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = degree != 0
                ) {
                    Text("Узнать результат")
                }
            }

            if (state == NeckState.RESULT) {

                val resultText = when {
                    degree >= 80 ->
                        "Отличная подвижность шеи. Диапазон движений полный, суставы работают без ограничений."

                    degree >= 60 ->
                        "Хорошая подвижность. Есть небольшие ограничения, но функционально шея здорова."

                    degree >= 40 ->
                        "Средняя подвижность. Возможны мышечные зажимы и недостаток регулярной разминки."

                    else ->
                        "Низкая подвижность. Вероятна скованность шейного отдела и перегрузка мышц."
                }

                val advice = """
                    Рекомендации:
                    • делайте мягкие вращения шеи ежедневно
                    • избегайте длительного наклона головы вперёд (телефон/ноутбук)
                    • добавьте растяжку трапециевидных мышц
                    • следите за осанкой в течение дня
                """.trimIndent()

                Card {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            "Результат: $degree°",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(resultText, style = MaterialTheme.typography.bodyMedium)

                        Spacer(Modifier.height(12.dp))

                        Text("Рекомендации", fontWeight = FontWeight.Bold)
                        Text(advice, style = MaterialTheme.typography.bodyMedium)
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