package com.pixelrabbit.oculi.domain.use_cases

import com.pixelrabbit.oculi.domain.models.UserProgress
import com.pixelrabbit.oculi.domain.repositories.UserRepository

class GetUserProgressUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): UserProgress {
        return repository.getUserProgress()
    }
}