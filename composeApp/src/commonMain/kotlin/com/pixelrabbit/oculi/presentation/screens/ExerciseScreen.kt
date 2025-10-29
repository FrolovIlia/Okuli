package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen

data class ExerciseScreen(
    val exerciseId: String
) : Screen {
    @Composable
    override fun Content() {
        ExerciseContent(exerciseId = exerciseId)
    }
}

@Composable
fun ExerciseContent(
    exerciseId: String,
    onBack: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Упражнение: $exerciseId")
        Button(
            onClick = onBack,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Назад")
        }
    }
}