package com.pixelrabbit.backy.presentation.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

actual object PayScreen : Screen {
    @Composable
    override fun Content() {
        // iOS заглушка
        androidx.compose.material3.Text("Оплата на iOS временно недоступна")
    }
}