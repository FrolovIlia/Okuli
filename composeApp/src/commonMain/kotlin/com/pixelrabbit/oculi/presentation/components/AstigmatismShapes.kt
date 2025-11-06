// Добавь этот файл: AstigmatismShapes.kt
package com.pixelrabbit.oculi.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SiemensStar(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(200.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        // Рисуем звезду Сименса
        for (i in 0 until 36) {
            val angle = i * 10f
            rotate(angle, center) {
                drawLine(
                    color = Color.Black,
                    start = center,
                    end = Offset(center.x + radius, center.y),
                    strokeWidth = 2f
                )
            }
        }

        // Центральный круг
        drawCircle(
            color = Color.Black,
            center = center,
            radius = radius * 0.1f
        )
    }
}

@Composable
fun RadialLines(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(200.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        // Радиальные линии
        for (i in 0 until 24) {
            val angle = i * 15f
            rotate(angle, center) {
                drawLine(
                    color = Color.Black,
                    start = center,
                    end = Offset(center.x + radius, center.y),
                    strokeWidth = 2f
                )
            }
        }
    }
}

@Composable
fun ParallelLines(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(200.dp)) {
        val lineSpacing = size.height / 12

        // Вертикальные параллельные линии
        for (i in 0 until 8) {
            val x = size.width * (i + 1) / 9
            drawLine(
                color = Color.Black,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 3f
            )
        }

        // Горизонтальные параллельные линии
        for (i in 0 until 8) {
            val y = size.height * (i + 1) / 9
            drawLine(
                color = Color.Black,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 3f
            )
        }
    }
}

@Composable
fun ConcentricCircles(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(200.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.minDimension / 2

        // Концентрические круги
        for (i in 1..5) {
            val radius = maxRadius * i / 6
            drawCircle(
                color = Color.Black,
                center = center,
                radius = radius,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )
        }

        // Радиальные линии через центр
        for (i in 0 until 8) {
            val angle = i * 45f
            rotate(angle, center) {
                drawLine(
                    color = Color.Black,
                    start = center,
                    end = Offset(center.x + maxRadius, center.y),
                    strokeWidth = 2f
                )
            }
        }
    }
}