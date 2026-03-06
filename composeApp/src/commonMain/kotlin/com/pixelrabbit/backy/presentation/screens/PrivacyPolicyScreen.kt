package com.pixelrabbit.backy.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

object PrivacyPolicyScreen : Screen {
    @Composable
    override fun Content() {
        PrivacyPolicyContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyContent() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Политика конфиденциальности",
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
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Политика конфиденциальности backy",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Дата вступления в силу: 1 января 2024 года",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column {
                        Text(
                            text = "1. Собираемая информация",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Приложение backy собирает следующие типы данных:
                                
                                • Данные об использовании приложения (время тренировок, частота использования)
                                • Результаты выполнения упражнений и тестов зрения
                                • Настройки пользователя и предпочтения
                                • Техническая информация (версия ОС, модель устройства)
                                
                                Мы не собираем личную идентифицирующую информацию без вашего согласия.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "2. Использование информации",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Собранные данные используются для:
                                
                                • Улучшения работы приложения и пользовательского опыта
                                • Персонализации тренировочных программ
                                • Анализа эффективности упражнений
                                • Разработки новых функций
                                • Обеспечения технической поддержки
                                
                                Мы используем агрегированные анонимные данные для аналитики.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "3. Хранение и защита данных",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Мы серьезно относимся к защите ваших данных:
                                
                                • Основные данные хранятся локально на вашем устройстве
                                • Резервные копии (если включены) шифруются
                                • Мы не передаем ваши данные третьим лицам без вашего согласия
                                • Используем современные методы шифрования для защиты
                                
                                Вы можете в любой момент удалить свои данные через настройки приложения.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "4. Ваши права",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Вы имеете право:
                                
                                • На доступ к вашим персональным данным
                                • На исправление неточных данных
                                • На удаление ваших данных
                                • На ограничение обработки данных
                                • На перенос данных
                                • На отзыв согласия на обработку данных
                                
                                Для реализации этих прав свяжитесь с нами по email.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "5. Контакты",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Если у вас есть вопросы о нашей политике конфиденциальности, 
                                пожалуйста, свяжитесь с нами:
                                
                                Email: pixel.rabbit.soft@gmail.com
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Text(
                        text = "Мы можем периодически обновлять эту политику конфиденциальности. " +
                                "О любых изменениях мы уведомим вас через приложение.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
