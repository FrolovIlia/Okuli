package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
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
import backy.composeapp.generated.resources.posture_check

object PostureTestScreen : Screen {
    @Composable
    override fun Content() = PostureTestContent()
}

private enum class PostureState { IDLE, RESULT }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostureTestContent() {

    val navigator = LocalNavigator.currentOrThrow

    var answers by rememberSaveable { mutableStateOf(List(8) { 0 }) }
    var state by rememberSaveable { mutableStateOf(PostureState.IDLE) }

    val questions = listOf(
        "Подбородок выдвинут вперёд?",
        "Плечи округлены вперёд?",
        "Голова наклонена при работе?",
        "Есть дискомфорт в шее?",
        "Есть напряжение между лопатками?",
        "Сутулитесь при сидении?",
        "Быстро устает спина?",
        "Сложно держать ровную осанку длительно?"
    )

    val options = listOf("Нет" to 0, "Иногда" to 1, "Да" to 2)

    val totalScore = answers.sum()

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
                "🧘 Тест осанки",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(Res.drawable.posture_check),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            if (state == PostureState.IDLE) {

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text(
                            "Как пройти тест",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Оцените своё состояние осанки в повседневной жизни. " +
                                    "Отвечайте честно — это влияет на точность результата."
                        )
                    }
                }

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        questions.forEachIndexed { idx, q ->

                            Column {

                                Text(
                                    text = "${idx + 1}. $q",
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(Modifier.height(10.dp))

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {

                                    options.forEach { (label, score) ->

                                        val selected = answers[idx] == score

                                        Button(
                                            onClick = {
                                                answers = answers.toMutableList().apply {
                                                    set(idx, score)
                                                }
                                            },
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
                                            Text(label)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { state = PostureState.RESULT },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Узнать результат")
                }
            }

            if (state == PostureState.RESULT) {

                val total = answers.sumOf { it }

                val resultText = when {
                    total <= 1 ->
                        "Хорошая осанка. Мышечный баланс сохранён."

                    total <= 3 ->
                        "Лёгкие признаки нарушения осанки."

                    total <= 5 ->
                        "Умеренные нарушения осанки."

                    total <= 7 ->
                        "Выраженные нарушения осанки."

                    else ->
                        "Сильные нарушения осанки и перегрузка шейно-грудного отдела."
                }

                val recommendations = listOf(
                    "Следите за положением головы при работе с телефоном",
                    "Делайте перерывы каждые 30–40 минут",
                    "Добавьте упражнения на раскрытие грудного отдела",
                    "Укрепляйте мышцы спины и кора",
                    "Контролируйте положение плеч в течение дня"
                )

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {

                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Text(
                            "Ваш результат",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        HorizontalDivider()

                        Text(resultText)

                        HorizontalDivider()

                        Text(
                            "Рекомендации",
                            fontWeight = FontWeight.Bold
                        )

                        recommendations.forEach {
                            Text("• $it")
                        }
                    }
                }

                Button(
                    onClick = { state = PostureState.IDLE },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Пройти заново")
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