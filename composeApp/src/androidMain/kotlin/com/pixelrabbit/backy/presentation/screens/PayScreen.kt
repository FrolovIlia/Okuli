package com.pixelrabbit.backy.presentation.screens

import android.app.Activity
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.pixelrabbit.backy.di.ServiceLocator
import com.pixelrabbit.backy.payment.PaymentController
import kotlinx.coroutines.launch

object PayScreen : Screen {
    @Composable
    override fun Content() {
        PayContent()
    }
}

enum class PaymentState {
    IDLE, PROCESSING, SUCCESS, ERROR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayContent() {
    val navigator = LocalNavigator.currentOrThrow
    val activity = LocalContext.current as Activity
    val paymentController = remember { PaymentController() }
    val scope = rememberCoroutineScope()

    paymentController.setActivity(activity)

    var paymentState by remember { mutableStateOf(PaymentState.IDLE) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val isPurchased = ServiceLocator.settingsRepository().isAdsRemoved()
        Log.d("!!!PAYMENT!!!", "Check purchase on screen open: isPurchased=$isPurchased")
        if (isPurchased) {
            paymentState = PaymentState.SUCCESS
        }
        isLoading = false
    }

    val iconScale by animateFloatAsState(
        targetValue = if (paymentState == PaymentState.SUCCESS) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "iconScale"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Отключить рекламу",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Единая карточка статуса
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 2.dp,
                                    color = if (paymentState == PaymentState.SUCCESS)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(
                                targetState = paymentState,
                                transitionSpec = {
                                    fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut()
                                },
                                label = "iconTransition"
                            ) { state ->
                                when (state) {
                                    PaymentState.SUCCESS -> {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Успешно",
                                            modifier = Modifier
                                                .size(40.dp)
                                                .scale(iconScale),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    else -> {
                                        Icon(
                                            imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = "Корзина",
                                            modifier = Modifier.size(40.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Тексты в зависимости от состояния
                        when (paymentState) {
                            PaymentState.SUCCESS -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Premium активирован!",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Реклама отключена навсегда",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Спасибо за поддержку! 🎉",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            else -> {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "Premium доступ",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Никакой рекламы. Навсегда.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                // Преимущества (не показываем при успехе и при обработке)
                AnimatedVisibility(
                    visible = paymentState != PaymentState.SUCCESS && paymentState != PaymentState.PROCESSING,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Что вы получите:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            BenefitRow(text = "Полное отключение всей рекламы навсегда")
                            BenefitRow(text = "Все текущие и будущие упражнения")
                            BenefitRow(text = "Поддержка разработчика")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка оплаты и цена (не показываем при успехе и при обработке)
                AnimatedVisibility(
                    visible = paymentState != PaymentState.SUCCESS && paymentState != PaymentState.PROCESSING,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "249 ₽",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Text(
                                text = "единоразово",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    Log.d("!!!PAYMENT!!!", "КНОПКА НАЖАТА")
                                    paymentState = PaymentState.PROCESSING
                                    errorMessage = null

                                    paymentController.startPayment(
                                        amount = "249.00",
                                        description = "Отключение рекламы в Backy",
                                        onSuccess = { transactionId ->
                                            Log.d("!!!PAYMENT!!!", "onSuccess: $transactionId")
                                            scope.launch {
                                                ServiceLocator.settingsRepository().setAdsRemoved(true)
                                                ServiceLocator.settingsRepository().setTransactionId(transactionId)
                                                paymentState = PaymentState.SUCCESS
                                            }
                                        },
                                        onFailure = { error ->
                                            Log.d("!!!PAYMENT!!!", "onFailure: $error")
                                            errorMessage = error
                                            paymentState = PaymentState.ERROR
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                enabled = paymentState == PaymentState.IDLE || paymentState == PaymentState.ERROR
                            ) {
                                when (paymentState) {
                                    PaymentState.IDLE -> {
                                        Icon(
                                            imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Оплатить",
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                    PaymentState.ERROR -> {
                                        Icon(Icons.Default.Close, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Ошибка, попробовать снова")
                                    }
                                    else -> {}
                                }
                            }

                            if (paymentState == PaymentState.ERROR && errorMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = errorMessage ?: "Неизвестная ошибка",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                // Процесс оплаты (отдельный блок)
                AnimatedVisibility(
                    visible = paymentState == PaymentState.PROCESSING,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Секунду...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // Информационный текст (не показываем при успехе и при обработке)
                AnimatedVisibility(
                    visible = paymentState != PaymentState.SUCCESS && paymentState != PaymentState.PROCESSING,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "Оплата через ЮKassa",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun BenefitRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}