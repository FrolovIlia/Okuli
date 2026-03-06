package com.pixelrabbit.backy.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlin.random.Random
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI

import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.withTransform
import backy.composeapp.generated.resources.Res
import backy.composeapp.generated.resources.joy_backy
import com.pixelrabbit.backy.domain.models.Achievement
import androidx.compose.ui.zIndex


@Composable
fun CelebrationDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    newAchievement: Achievement? = null
) {
    val motivationalText by remember { mutableStateOf(getRandomMotivationalText()) }

    val fireworkPositions by remember { mutableStateOf(generateRandomFireworkPositions(6)) }

    val backgroundAlpha by animateFloatAsState(
        targetValue = 0.8f,
        animationSpec = tween(500),
        label = "background_alpha"
    )
    val dialogAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(500, delayMillis = 100),
        label = "dialog_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() }
    ) {
        // Затемнённый фон
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha))
        )

        // Контейнер для вертикального расположения элементов
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Плашка с достижением (если есть) - в верхней части
            if (newAchievement != null) {
                AchievementBanner(
                    achievement = newAchievement,
                    modifier = Modifier
                        .padding(top = 60.dp) // Отступ от верхнего края
                        .zIndex(1f)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .size(340.dp)
                        .alpha(dialogAlpha)
                        .clickable(enabled = false) {},
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        // Фоновый градиент
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                                            MaterialTheme.colorScheme.surface
                                        )
                                    )
                                )
                        )

                        // Фейерверки на фоне
                        BackgroundFireworks(fireworkPositions)

                        // Основной контент
                        CelebrationContent(motivationalText)
                    }
                }
            }
        }
    }
}

@Composable
private fun CelebrationContent(motivationalText: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ConfettiRow(
            modifier = Modifier.fillMaxWidth(),
            startDelay = 0
        )

        CenterContent(motivationalText)

        ConfettiRow(
            modifier = Modifier.fillMaxWidth(),
            startDelay = 1000
        )
    }
}

@Composable
private fun CenterContent(motivationalText: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AppLogo()

        Text(
            text = motivationalText,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppLogo() {
    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center
    ) {
        FallingParticlesAnimation(
            modifier = Modifier.matchParentSize()
        )

        Image(
            painter = painterResource(Res.drawable.joy_backy),
            contentDescription = "Логотип backy",
            modifier = Modifier.size(1000.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun BackgroundFireworks(positions: List<FireworkPosition>) {
    Box(modifier = Modifier.fillMaxSize()) {
        positions.forEachIndexed { index, position ->
            FireworkAnimation(
                modifier = Modifier
                    .size(120.dp)
                    .align(position.alignment)
                    .offset(
                        x = position.offsetX.dp,
                        y = position.offsetY.dp
                    ),
                startDelay = index * 400,
                particleCount = 15,
                maxDistance = 50f
            )
        }
    }
}

@Composable
private fun ConfettiRow(modifier: Modifier = Modifier, startDelay: Int = 0) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ConfettiAnimation(
            modifier = Modifier.size(80.dp),
            startDelay = startDelay
        )
        ConfettiAnimation(
            modifier = Modifier.size(80.dp),
            startDelay = startDelay + 500
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConfettiAnimation(
    modifier: Modifier = Modifier,
    startDelay: Int = 0
) {
    val particles = remember { generateConfettiPieces(20) }
    val infiniteTransition = rememberInfiniteTransition()

    val cycleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(startDelay)
        )
    )

    Canvas(modifier = modifier) {
        val cycleDuration = 4000
        particles.forEach { confetti ->
            val fallProgressRaw = (cycleProgress * cycleDuration + confetti.startDelay) % confetti.fallDuration / confetti.fallDuration.toFloat()
            val fallProgressClamped = fallProgressRaw.coerceIn(0f, 1f)
            val y = size.height * (confetti.y + fallProgressClamped * (1.1f - confetti.y))

            val swingOffset = sin(cycleProgress * PI.toFloat() * 4f) * confetti.swingAmplitude * size.width
            val x = confetti.x * size.width + swingOffset

            val alpha = (1f - (fallProgressClamped - 0.8f).coerceIn(0f, 0.2f) / 0.2f).coerceIn(0f, 1f)

            withTransform({
                translate(left = x, top = y)
            }) {
                drawRect(
                    color = confetti.color.copy(alpha = alpha),
                    topLeft = Offset(-confetti.size / 2, -confetti.size / 2),
                    size = androidx.compose.ui.geometry.Size(confetti.size, confetti.size)
                )
            }
        }
    }
}

@Composable
fun FireworkAnimation(
    modifier: Modifier = Modifier,
    startDelay: Int = 0,
    particleCount: Int = 12,
    maxDistance: Float = 40f
) {
    val particles = remember { generateFireworkParticles(particleCount, maxDistance) }
    val infiniteTransition = rememberInfiniteTransition()

    val explosionProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                0.0f at 0 with LinearEasing
                1.0f at 1800
            },
            repeatMode = RepeatMode.Restart,
            initialStartOffset = StartOffset(startDelay)
        )
    )

    Canvas(modifier = modifier) {
        val animationTime = 1800
        particles.forEach { particle ->
            val progressRaw = ((explosionProgress * animationTime).toLong() - particle.initialDelay)
                .coerceIn(0L, animationTime.toLong()) / animationTime.toFloat()

            val travelProgress = FastOutSlowInEasing.transform((progressRaw / 0.8f).coerceIn(0f, 1f))

            if (progressRaw < 0.8f) {
                val currentDistance = particle.maxDistance * travelProgress * 1.5f
                val alpha = (1f - progressRaw * 1.25f).coerceIn(0f, 1f)
                val currentSize = particle.size * (1f - travelProgress * 0.5f)

                val angleRad = (particle.angle * PI.toFloat() / 180f)

                val x = center.x + currentDistance * cos(angleRad)
                val y = center.y + currentDistance * sin(angleRad)

                drawCircle(
                    color = particle.color.copy(alpha = alpha),
                    radius = currentSize,
                    center = Offset(x, y)
                )

                if (progressRaw < 0.1f) {
                    val flashAlpha = (0.1f - progressRaw) / 0.1f
                    drawCircle(
                        color = Color.White.copy(alpha = flashAlpha),
                        radius = size.minDimension * 0.1f * (1f - progressRaw * 10f),
                        center = center
                    )
                }
            }
        }
    }
}

@Composable
fun FallingParticlesAnimation(modifier: Modifier = Modifier) {
    val particles = remember { generateFallingParticles(10) }
    val infiniteTransition = rememberInfiniteTransition()

    val fallProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier) {
        particles.forEach { particle ->
            val currentY = particle.startY + (particle.endY - particle.startY) * fallProgress
            val currentX = particle.startX + particle.swing * sin(fallProgress * PI.toFloat() * 2f)
            val alpha = 1f - (fallProgress - 0.7f).coerceIn(0f, 0.3f) / 0.3f

            if (alpha > 0) {
                drawCircle(
                    color = particle.color.copy(alpha = alpha),
                    radius = particle.size,
                    center = Offset(currentX * size.width, currentY * size.height)
                )
            }
        }
    }
}

@Composable
private fun AchievementBanner(
    achievement: Achievement,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = achievement.icon,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Новое достижение!",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class FireworkParticle(
    val angle: Float,
    val maxDistance: Float,
    val size: Float,
    val color: Color,
    val initialDelay: Int
)

private data class FallingParticle(
    val startX: Float,
    val startY: Float,
    val endY: Float,
    val size: Float,
    val color: Color,
    val swing: Float
)

private data class ConfettiPiece(
    val x: Float,
    val y: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float,
    val swingAmplitude: Float,
    val fallDuration: Int,
    val startDelay: Int
)

private data class FireworkPosition(
    val alignment: Alignment,
    val offsetX: Int,
    val offsetY: Int
)

private fun generateRandomFireworkPositions(count: Int): List<FireworkPosition> {
    val positions = mutableListOf<FireworkPosition>()
    val usedAlignments = mutableSetOf<Alignment>()

    val possibleAlignments = listOf(
        Alignment.TopStart to Pair(-30, -30),
        Alignment.TopCenter to Pair(0, -40),
        Alignment.TopEnd to Pair(30, -30),
        Alignment.CenterStart to Pair(-40, 0),
        Alignment.CenterEnd to Pair(40, 0),
        Alignment.BottomStart to Pair(-30, 30),
        Alignment.BottomCenter to Pair(0, 40),
        Alignment.BottomEnd to Pair(30, 30)
    )

    repeat(count) {
        val available = possibleAlignments.filter { it.first !in usedAlignments }
        if (available.isNotEmpty()) {
            val random = available.random()
            positions.add(FireworkPosition(random.first, random.second.first, random.second.second))
            usedAlignments.add(random.first)
        }
    }

    return positions
}

private fun generateFireworkParticles(count: Int, maxDistance: Float): List<FireworkParticle> {
    val colors = listOf(
        Color(0xFFFF5252), Color(0xFFFFEB3B), Color(0xFF4CAF50),
        Color(0xFF2196F3), Color(0xFF9C27B0), Color(0xFFFF9800)
    )

    return List(count) {
        FireworkParticle(
            angle = it * (360f / count) + Random.nextFloat() * 15f,
            maxDistance = maxDistance,
            size = Random.nextFloat() * 4f + 3f,
            color = colors.random(),
            initialDelay = Random.nextInt(0, 300)
        )
    }
}

private fun generateFallingParticles(count: Int): List<FallingParticle> {
    val colors = listOf(
        Color(0xFFFFD700), Color(0xFFFF6B6B), Color(0xFF4ECDC4), Color(0xFF45B7D1)
    )

    return List(count) {
        FallingParticle(
            startX = Random.nextFloat() * 0.8f + 0.1f,
            startY = -0.1f - Random.nextFloat() * 0.2f,
            endY = 1.1f + Random.nextFloat() * 0.2f,
            size = Random.nextFloat() * 3f + 2f,
            color = colors.random(),
            swing = Random.nextFloat() * 0.1f - 0.05f
        )
    }
}

private fun generateConfettiPieces(count: Int): List<ConfettiPiece> {
    val colors = listOf(
        Color(0xFFFFD700), Color(0xFFFF6B6B), Color(0xFF4ECDC4),
        Color(0xFF45B7D1), Color(0xFFFFA726), Color(0xFFAB47BC)
    )

    return List(count) {
        ConfettiPiece(
            x = Random.nextFloat(),
            y = Random.nextFloat() * 0.5f,
            size = Random.nextFloat() * 10f + 5f,
            color = colors.random(),
            rotationSpeed = Random.nextFloat() * 360f + 180f,
            swingAmplitude = Random.nextFloat() * 0.05f + 0.03f,
            fallDuration = Random.nextInt(2500, 4500),
            startDelay = Random.nextInt(0, 1500)
        )
    }
}

private fun getRandomMotivationalText(): String {
    val motivationalTexts = listOf(
        "Так держать!", "Отличная работа!", "Превосходно!",
        "Продолжай в том же духе!", "Фантастически!", "Замечательный результат!",
        "Ты справляешься великолепно!", "Это был успех!"
    )
    return motivationalTexts.random()
}