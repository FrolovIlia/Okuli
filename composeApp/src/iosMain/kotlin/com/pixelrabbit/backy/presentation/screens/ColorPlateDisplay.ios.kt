package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Какую цифру вы видите?",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Box(
                modifier = Modifier
                    .height(320.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🎨", style = MaterialTheme.typography.displayLarge)
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                enabled = selectedAnswer != null,
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedAnswer != null)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (selectedAnswer != null)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
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
    val (bg, fg, border) = when {
        !answerChecked && isSelected ->
            Triple(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary
            )

        answerChecked && isCorrect ->
            Triple(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.onPrimaryContainer,
                MaterialTheme.colorScheme.primary
            )

        answerChecked && isSelected ->
            Triple(
                MaterialTheme.colorScheme.errorContainer,
                MaterialTheme.colorScheme.onErrorContainer,
                MaterialTheme.colorScheme.error
            )

        else ->
            Triple(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.onSurface,
                Color.Transparent
            )
    }

    Button(
        onClick = onClick,
        enabled = !answerChecked,
        modifier = modifier.height(52.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = bg,
            contentColor = fg
        ),
        border = BorderStroke(
            if (border != Color.Transparent) 2.dp else 1.dp,
            border
        )
    ) {
        Text(text, fontWeight = FontWeight.Medium)
    }
}
