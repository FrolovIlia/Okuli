package com.pixelrabbit.oculi.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object ExercisesListScreen : Screen {
    @Composable
    override fun Content() {
        ExercisesListContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesListContent() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Упражнения для глаз") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    navigator.push(ExerciseDetailScreen("follow_target"))
                }
            ) {
                Text("🎯 Преследуй цель")
            }

            Button(
                onClick = {
                    navigator.push(ExerciseDetailScreen("focus_shift"))
                }
            ) {
                Text("🔍 Фокус-сдвиг")
            }

            Button(
                onClick = {
                    navigator.push(ExerciseDetailScreen("palming"))
                }
            ) {
                Text("👐 Пальминг")
            }

            Button(
                onClick = {
                    navigator.push(ExerciseDetailScreen("figure_eight"))
                }
            ) {
                Text("∞ Восьмерки")
            }

            Button(
                onClick = { navigator.pop() }
            ) {
                Text("Назад")
            }
        }
    }
}