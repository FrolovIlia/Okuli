package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.models.UserProgress
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import kotlinx.coroutines.flow.Flow

class GetStatsUseCase(private val repository: StatsRepository) {
    suspend operator fun invoke(): UserProgress {
        return repository.getUserProgress()
    }

    fun getStatsFlow(): Flow<UserProgress> = repository.getUserProgressFlow()
}