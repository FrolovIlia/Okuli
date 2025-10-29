package com.pixelrabbit.oculi.domain.models

data class SubscriptionStatus(
    val isActive: Boolean,
    val expiresAt: String? = null,
    val plan: String? = null
)