package com.pixelrabbit.backy.domain.models

import kotlinx.datetime.Instant

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val unlockedAt: Instant?,
    val progress: Float,
    val targetValue: Int,
    val currentValue: Int,
    val category: String
)