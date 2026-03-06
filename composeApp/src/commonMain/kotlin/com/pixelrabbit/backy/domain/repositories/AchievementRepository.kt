package com.pixelrabbit.backy.domain.repositories

import com.pixelrabbit.backy.domain.models.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    suspend fun getAchievements(): List<Achievement>
    fun getAchievementsFlow(): Flow<List<Achievement>>
    suspend fun checkAndUnlockAchievements()
    suspend fun isAchievementShown(achievementId: String): Boolean
    suspend fun markAchievementAsShown(achievementId: String)
}