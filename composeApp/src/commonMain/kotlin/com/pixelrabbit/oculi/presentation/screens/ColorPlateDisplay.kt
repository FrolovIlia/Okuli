package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class ColorPlate(
    val number: Int,
    val correctAnswer: String,
    val description: String,
    val options: List<String>
)

@Composable
expect fun ColorPlateDisplay(
    plate: ColorPlate,
    selectedAnswer: String?,
    answerChecked: Boolean,
    onAnswerSelect: (String) -> Unit,
    onNextPlate: () -> Unit,
    modifier: Modifier = Modifier
)

