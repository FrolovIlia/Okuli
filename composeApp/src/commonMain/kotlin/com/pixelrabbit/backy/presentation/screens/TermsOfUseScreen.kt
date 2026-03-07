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

object TermsOfUseScreen : Screen {
    @Composable
    override fun Content() {
        TermsOfUseContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfUseContent() {
    val navigator = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Условия использования",
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
                        text = "Условия использования Backy",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Дата последнего обновления: 01.01.2026",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column {
                        Text(
                            text = "1. Принятие условий",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Используя приложение Backy, вы подтверждаете, что прочитали, 
                                поняли и соглашаетесь соблюдать настоящие Условия использования.
                                
                                Если вы не согласны с этими условиями, пожалуйста, не используйте 
                                наше приложение.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "2. Медицинское предупреждение",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Важное предупреждение:
                                
                                • Backy является приложением для тренировки осанки и информационных целей
                                • Приложение НЕ заменяет профессиональную медицинскую консультацию
                                • Перед началом любых упражнений проконсультируйтесь с физиотерапевтом
                                • При появлении боли, дискомфорта или ухудшения самочувствия немедленно 
                                  прекратите использование приложения и обратитесь к врачу
                                
                                Вы используете приложение на свой собственный риск.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "3. Права и ограничения",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Вам разрешается:
                                
                                • Использовать приложение для личных, некоммерческих целей
                                • Устанавливать приложение на свои личные устройства
                                • Делать резервные копии своих данных
                                
                                Вам запрещается:
                                
                                • Модифицировать, копировать или распространять приложение
                                • Использовать приложение в коммерческих целях без разрешения
                                • Обратно разрабатывать или извлекать исходный код
                                • Нарушать права интеллектуальной собственности
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "4. Интеллектуальная собственность",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Все права на приложение Backy защищены:
                                
                                • Логотип, название и дизайн являются собственностью разработчиков
                                • Все упражнения, методики и контент защищены авторским правом
                                • Запрещено использование наших материалов без письменного разрешения
                                
                                Pixel Rabbit Soft сохраняет за собой все права, не предоставленные 
                                явно в настоящих условиях.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "5. Ответственность",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Разработчики не несут ответственности:
                                
                                • За любые прямые или косвенные последствия использования приложения
                                • За потерю данных или технические сбои
                                • За неправильное использование приложения пользователем
                                • За медицинские последствия, связанные с использованием приложения
                                
                                Приложение предоставляется "как есть" без гарантий любого рода.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "6. Изменения условий",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Мы оставляем за собой право:
                                
                                • Изменять настоящие Условия использования в любое время
                                • Уведомлять пользователей об изменениях через приложение
                                • Прекращать поддержку приложения с уведомлением пользователей
                                
                                Продолжение использования приложения после изменений означает 
                                ваше согласие с новыми условиями.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "7. Прекращение использования",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                Мы можем приостановить или прекратить ваш доступ к приложению:
                                
                                • При нарушении вами настоящих Условий
                                • По техническим или юридическим причинам
                                • При прекращении поддержки приложения
                                
                                Вы можете прекратить использование приложения в любое время, 
                                удалив его со своего устройства.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Column {
                        Text(
                            text = "8. Контакты",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = """
                                По вопросам, связанным с Условиями использования:
                                
                                Email: pixel.rabbit.soft@gmail.com
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }

                    Text(
                        text = "Благодарим вас за использование Backy и заботу о здоровье вашей спины и шеи!",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
            }
        }
    }
}
