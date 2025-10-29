package com.pixelrabbit.oculi.domain.repositories

import com.pixelrabbit.oculi.domain.models.UserProgress
import kotlinx.datetime.LocalDateTime

interface UserRepository {
    suspend fun getUserProgress(): UserProgress
    suspend fun updateUserProgress(progress: UserProgress)
    suspend fun isPremiumUser(): Boolean
    suspend fun getSubscriptionStatus(): SubscriptionStatus
}

data class SubscriptionStatus(
    val isActive: Boolean,
    val expiresAt: LocalDateTime? = null,
    val plan: String? = null
)