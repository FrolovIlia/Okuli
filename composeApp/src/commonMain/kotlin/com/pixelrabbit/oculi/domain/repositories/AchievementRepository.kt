package com.pixelrabbit.oculi.domain.repositories

import com.pixelrabbit.oculi.domain.models.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    suspend fun getAchievements(): List<Achievement>
    suspend fun updateAchievementProgress(achievementId: String, progress: Int)
    suspend fun unlockAchievement(achievementId: String)
    fun getAchievementsFlow(): Flow<List<Achievement>>
}