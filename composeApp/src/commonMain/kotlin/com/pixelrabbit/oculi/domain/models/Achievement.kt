package com.pixelrabbit.oculi.domain.models

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val unlockedAt: String? = null
)