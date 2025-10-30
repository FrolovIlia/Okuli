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

object AstigmatismTestScreen : Screen {
    @Composable
    override fun Content() {
        AstigmatismTestContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstigmatismTestContent() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Тест на астигматизм") }
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
                        text = "🔴",
                        style = MaterialTheme.typography.displayLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Тест на астигматизм",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Посмотрите на изображение. Все линии должны быть одинаково четкими и прямыми. Если некоторые линии выглядят размытыми или искаженными, это может указывать на астигматизм.",
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