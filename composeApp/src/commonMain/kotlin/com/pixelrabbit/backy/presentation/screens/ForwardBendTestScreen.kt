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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.FlowRow
import backy.composeapp.generated.resources.spine_flexibility

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
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                "Наклонитесь вперёд и оцените расстояние до пола.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (state == BendState.IDLE) {

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    options.forEach { value ->

                        val selected = distanceCm == value

                        Button(
                            onClick = { distanceCm = value },
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
                            Text("$value см", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Button(
                    onClick = { state = BendState.RESULT },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = distanceCm != 0
                ) {
                    Text("Узнать результат")
                }
            }

            if (state == BendState.RESULT) {

                val resultText = when {
                    distanceCm <= 5 ->
                        "Отличная гибкость. Позвоночник и задняя линия тела работают свободно."

                    distanceCm <= 15 ->
                        "Хорошая гибкость. Есть небольшое ограничение, но функционально норма."

                    distanceCm <= 25 ->
                        "Средняя гибкость. Присутствует мышечное напряжение задней поверхности."

                    else ->
                        "Низкая гибкость. Возможна скованность поясницы и задней цепи мышц."
                }

                val advice = """
                    Рекомендации:
                    • ежедневная растяжка задней поверхности бедра
                    • упражнения на наклоны вперёд без рывков
                    • укрепление мышц кора
                    • избегать длительного сидения без движения
                """.trimIndent()

                Card {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            "Результат: $distanceCm см",
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