package com.pixelrabbit.oculi.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseVisualization(exerciseId: String, isRunning: Boolean) {
    when (exerciseId) {
        "follow_target" -> MovingTargetVisualization(isRunning)
        "focus_shift" -> FocusShiftVisualization(isRunning)
        "palming" -> PalmingVisualization(isRunning)
        "figure_eight" -> FigureEightVisualization(isRunning)
        else -> DefaultVisualization(isRunning)
    }
}

@Composable
fun MovingTargetVisualization(isRunning: Boolean) {
    val animation = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            animation.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            animation.stop()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 4
            val x = center.x + (size.width / 2 - radius) * animation.value

            drawCircle(
                color = Color(0xFF006A6B),
                center = Offset(x, center.y),
                radius = radius
            )
        }
    }
}

@Composable
fun FocusShiftVisualization(isRunning: Boolean) {
    val animation = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            animation.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            animation.stop()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val baseRadius = size.minDimension / 8
            val animatedRadius = baseRadius * (1 + animation.value * 0.5f)

            // Ближний объект
            drawCircle(
                color = Color(0xFF006A6B),
                center = center,
                radius = animatedRadius
            )

            // Дальний объект
            drawCircle(
                color = Color(0xFF4A6363),
                center = Offset(center.x, center.y - size.height / 3),
                radius = baseRadius * (1 + (1 - animation.value) * 0.5f)
            )
        }
    }
}

@Composable
fun FigureEightVisualization(isRunning: Boolean) {
    val animation = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            animation.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            animation.stop()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val a = size.minDimension / 4
            val b = size.minDimension / 6

            val progress = animation.value * 2 * Math.PI.toFloat()
            val x = center.x + a * kotlin.math.sin(progress)
            val y = center.y + b * kotlin.math.sin(2 * progress)

            drawCircle(
                color = Color(0xFF006A6B),
                center = Offset(x, y),
                radius = size.minDimension / 12
            )
        }
    }
}

@Composable
fun PalmingVisualization(isRunning: Boolean) {
    val animation = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            animation.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            animation.stop()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val maxRadius = size.minDimension / 2
            val currentRadius = maxRadius * animation.value

            drawCircle(
                color = Color(0xFF006A6B).copy(alpha = 0.3f),
                center = center,
                radius = currentRadius
            )

            // Руки
            drawCircle(
                color = Color(0xFF4A6363),
                center = Offset(center.x - size.width / 4, center.y),
                radius = size.minDimension / 6
            )

            drawCircle(
                color = Color(0xFF4A6363),
                center = Offset(center.x + size.width / 4, center.y),
                radius = size.minDimension / 6
            )
        }
    }
}

@Composable
fun DefaultVisualization(isRunning: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val center = Offset(size.width / 2, size.height / 2)

            drawCircle(
                color = Color(0xFF006A6B),
                center = center,
                radius = size.minDimension / 4
            )
        }
    }
}