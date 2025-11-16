package com.pixelrabbit.oculi.domain.repositories

import com.pixelrabbit.oculi.domain.models.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    suspend fun getAchievements(): List<Achievement>
    fun getAchievementsFlow(): Flow<List<Achievement>>
    suspend fun checkAndUnlockAchievements()
}