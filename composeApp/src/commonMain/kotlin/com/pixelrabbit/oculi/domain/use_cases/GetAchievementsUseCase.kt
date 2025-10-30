package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.models.Achievement
import com.pixelrabbit.oculi.domain.repositories.AchievementRepository

class GetAchievementsUseCase(
    private val repository: AchievementRepository
) {
    suspend operator fun invoke(): List<Achievement> {
        return repository.getAchievements()
    }
}