package com.pixelrabbit.backy.presentation.components

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
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ExerciseVisualization(
    exerciseId: String,
    isRunning: Boolean
) {

    when (exerciseId) {

        "neck_tilts" -> NeckTiltVisualization(isRunning)

        "neck_rotation" -> NeckRotationVisualization(isRunning)

        "shoulder_rolls" -> ShoulderRollVisualization(isRunning)

        "shoulder_blades" -> ShoulderBladeVisualization(isRunning)

        else -> DefaultVisualization(isRunning)
    }
}

@Composable
fun NeckTiltVisualization(isRunning: Boolean) {

    val animation = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {

        if (isRunning) {
            animation.animateTo(
                1f,
                infiniteRepeatable(
                    animation = tween(1500),
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
            val offset = size.width / 4 * (animation.value * 2 - 1)

            drawCircle(
                color = Color(0xFF006A6B),
                radius = 30f,
                center = Offset(center.x + offset, center.y)
            )
        }
    }
}

@Composable
fun NeckRotationVisualization(isRunning: Boolean) {

    val animation = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {

        if (isRunning) {
            animation.animateTo(
                1f,
                infiniteRepeatable(
                    animation = tween(2000),
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
            val radius = size.minDimension / 3

            val angle = animation.value * 2 * Math.PI

            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()

            drawCircle(
                color = Color(0xFF006A6B),
                radius = 25f,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun ShoulderRollVisualization(isRunning: Boolean) {

    val animation = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {

        if (isRunning) {
            animation.animateTo(
                1f,
                infiniteRepeatable(
                    animation = tween(2500),
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
            val radius = size.minDimension / 4

            val angle = animation.value * 2 * Math.PI

            val x = center.x + radius * cos(angle).toFloat()
            val y = center.y + radius * sin(angle).toFloat()

            drawCircle(
                color = Color(0xFF4A6363),
                radius = 20f,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun ShoulderBladeVisualization(isRunning: Boolean) {

    val animation = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {

        if (isRunning) {
            animation.animateTo(
                1f,
                infiniteRepeatable(
                    animation = tween(1500),
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

            val offset = size.width / 4 * animation.value

            drawCircle(
                color = Color(0xFF006A6B),
                radius = 20f,
                center = Offset(center.x - offset, center.y)
            )

            drawCircle(
                color = Color(0xFF006A6B),
                radius = 20f,
                center = Offset(center.x + offset, center.y)
            )
        }
    }
}

@Composable
fun DefaultVisualization(isRunning: Boolean) {

    val color =
        if (isRunning) Color(0xFF006A6B)
        else Color.Gray.copy(alpha = 0.5f)

    Box(modifier = Modifier.fillMaxSize()) {

        Canvas(modifier = Modifier.size(200.dp)) {

            val center = Offset(size.width / 2, size.height / 2)

            drawCircle(
                color = color,
                radius = size.minDimension / 4,
                center = center
            )
        }
    }
}