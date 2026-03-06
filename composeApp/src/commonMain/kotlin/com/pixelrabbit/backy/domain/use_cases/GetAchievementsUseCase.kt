package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.domain.models.Achievement
import com.pixelrabbit.backy.domain.repositories.AchievementRepository
import kotlinx.coroutines.flow.Flow

class GetAchievementsUseCase(private val repository: AchievementRepository) {
    suspend operator fun invoke(): List<Achievement> {
        return repository.getAchievements()
    }

    fun getAchievementsFlow(): Flow<List<Achievement>> = repository.getAchievementsFlow()
}