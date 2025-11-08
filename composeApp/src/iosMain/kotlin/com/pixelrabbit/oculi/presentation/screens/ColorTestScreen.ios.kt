package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
actual fun ColorPlateDisplay(
    plate: ColorPlate,
    selectedAnswer: String?,
    answerChecked: Boolean,
    onAnswerSelect: (String) -> Unit,
    onNextPlate: () -> Unit,
    modifier: Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Какую цифру вы видите?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .height(320.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🎨",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = "Пластина ${plate.number}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "iOS версия",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
            ) {
                plate.options.forEach { option ->
                    AnswerButton(
                        text = option,
                        isSelected = selectedAnswer == option,
                        isCorrect = option == plate.correctAnswer,
                        answerChecked = answerChecked,
                        onClick = { onAnswerSelect(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            Button(
                onClick = onNextPlate,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                enabled = selectedAnswer != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedAnswer != null) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (selectedAnswer != null) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(
                    text = if (selectedAnswer != null) "Дальше →" else "Выберите ответ",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AnswerButton(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    answerChecked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, borderColor) = when {
        !answerChecked -> when {
            isSelected -> Triple(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary
            )
            else -> Triple(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.onSurface,
                Color.Transparent
            )
        }

        else -> when {
            // Правильный ответ (зеленый)
            isCorrect -> Triple(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.onPrimaryContainer,
                MaterialTheme.colorScheme.primary
            )
            // Неправильный выбранный ответ (красный)
            isSelected && !isCorrect -> Triple(
                MaterialTheme.colorScheme.errorContainer,
                MaterialTheme.colorScheme.onErrorContainer,
                MaterialTheme.colorScheme.error
            )
            // Остальные ответы (серые)
            else -> Triple(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.colorScheme.onSurfaceVariant,
                Color.Transparent
            )
        }
    }

    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        border = BorderStroke(
            width = if (borderColor != Color.Transparent) 2.dp else 1.dp,
            color = borderColor
        ),
        enabled = !answerChecked
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}