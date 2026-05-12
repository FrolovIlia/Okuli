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
import backy.composeapp.generated.resources.posture_check
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip

object PostureTestScreen : Screen {
    @Composable
    override fun Content() = PostureTestContent()
}

private enum class PostureState { IDLE, RESULT }

@Composable
fun PostureTestContent() {

    val navigator = LocalNavigator.currentOrThrow

    var answers by rememberSaveable { mutableStateOf(List(4) { 0 }) }
    var state by rememberSaveable { mutableStateOf(PostureState.IDLE) }

    val questions = listOf(
        "Подбородок выдвинут вперёд?",
        "Плечи округлены вперёд?",
        "Голова наклонена?",
        "Есть дискомфорт в шее?"
    )

    val options = listOf("Нет" to 0, "Иногда" to 1, "Да" to 2)

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
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                "Ответьте честно на вопросы ниже.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (state == PostureState.IDLE) {

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    questions.forEachIndexed { idx, q ->

                        Column {

                            Text(q, fontWeight = FontWeight.Medium)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                options.forEach { (label, score) ->

                                    val selected = answers[idx] == score

                                    Button(
                                        onClick = {
                                            answers = answers.toMutableList().apply {
                                                set(idx, score)
                                            }
                                        },
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
                                        Text(label)
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
                        "Хорошая осанка. Мышечный баланс сохранён, выраженных нарушений нет."

                    total <= 3 ->
                        "Незначительные нарушения осанки. Возможны мышечные зажимы и усталость."

                    else ->
                        "Выраженные нарушения осанки. Вероятна перегрузка шейного и грудного отдела."
                }

                val advice = """
                    Рекомендации:
                    • следите за положением головы при работе за телефоном
                    • делайте перерывы каждые 30–40 минут
                    • добавьте упражнения на раскрытие грудного отдела
                    • укрепляйте мышцы спины
                """.trimIndent()

                Card {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            "Результат",
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