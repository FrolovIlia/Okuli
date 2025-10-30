package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.models.Achievement
import com.pixelrabbit.oculi.domain.repositories.AchievementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RealAchievementRepository : AchievementRepository {
    private val _achievements = MutableStateFlow(initialAchievements())

    override suspend fun getAchievements(): List<Achievement> = _achievements.value
    override fun getAchievementsFlow(): Flow<List<Achievement>> = _achievements.asStateFlow()

    override suspend fun updateAchievementProgress(achievementId: String, progress: Int) {
        // Реализация обновления прогресса
    }

    override suspend fun unlockAchievement(achievementId: String) {
        val current = _achievements.value.toMutableList()
        val index = current.indexOfFirst { it.id == achievementId }
        if (index != -1) {
            val achievement = current[index]
            current[index] = achievement.copy(unlockedAt = "Сегодня")
            _achievements.value = current
        }
    }

    private fun initialAchievements(): List<Achievement> {
        return listOf(
            Achievement(
                id = "first_exercise",
                title = "Первые шаги",
                description = "Выполните первое упражнение",
                icon = "🎯"
            ),
            Achievement(
                id = "weekly_streak",
                title = "Неделя заботы",
                description = "Тренируйтесь 7 дней подряд",
                icon = "🔥"
            ),
            Achievement(
                id = "eye_master",
                title = "Мастер глаз",
                description = "Выполните 50 упражнений",
                icon = "👑"
            ),
            Achievement(
                id = "time_investor",
                title = "Инвестор в здоровье",
                description = "Потратьте 60 минут на тренировки",
                icon = "⏱️"
            ),
            Achievement(
                id = "vision_tracker",
                title = "Следящий за зрением",
                description = "Пройдите 5 проверок зрения",
                icon = "👁️"
            ),
            Achievement(
                id = "daily_champion",
                title = "Ежедневный чемпион",
                description = "Выполняйте упражнения 30 дней подряд",
                icon = "🏆"
            )
        )
    }
}