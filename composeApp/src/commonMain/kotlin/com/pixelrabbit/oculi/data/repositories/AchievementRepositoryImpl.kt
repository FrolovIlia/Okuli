package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.models.Achievement
import com.pixelrabbit.oculi.domain.repositories.AchievementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AchievementRepositoryImpl : AchievementRepository {

    private val achievements = listOf(
        Achievement(
            id = "first_exercise",
            title = "Первые шаги",
            description = "Выполните первое упражнение",
            icon = "🎯",
            unlockedAt = "2024-01-15" // Демонстрационная дата
        ),
        Achievement(
            id = "weekly_streak",
            title = "Неделя заботы",
            description = "Тренируйтесь 7 дней подряд",
            icon = "🔥",
            unlockedAt = null // Еще не разблокировано
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

    override suspend fun getAchievements(): List<Achievement> {
        return achievements
    }

    override suspend fun updateAchievementProgress(achievementId: String, progress: Int) {
        println("Обновление прогресса достижения $achievementId: $progress")
    }

    override suspend fun unlockAchievement(achievementId: String) {
        println("Разблокировано достижение: $achievementId")
    }

    override fun getAchievementsFlow(): Flow<List<Achievement>> {
        return flow { emit(achievements) }
    }
}