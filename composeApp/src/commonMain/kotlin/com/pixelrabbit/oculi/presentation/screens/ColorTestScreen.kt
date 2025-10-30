package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object ColorTestScreen : Screen {
    @Composable
    override fun Content() {
        ColorTestContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тест на цветовосприятие") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎨",
                        style = MaterialTheme.typography.displayLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Тест на цветовосприятие",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "В этом тесте вы будете видеть изображения с цветными точками. Вам нужно определить, какие цифры или фигуры вы видите на этих изображениях.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { /* TODO: Начать тест */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Начать тест")
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