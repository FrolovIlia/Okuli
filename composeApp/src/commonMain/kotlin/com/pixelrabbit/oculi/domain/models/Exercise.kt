package com.pixelrabbit.oculi.domain.models

data class Exercise(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int, // в секундах
    val type: ExerciseType,
    val difficulty: Difficulty,
    val isPremium: Boolean = false,
    val icon: String,
    val instructions: List<String>,
    val imageName: String? = null
)

enum class ExerciseType {
    ACCOMMODATION,
    RELAXATION,
    MOBILITY
}

enum class Difficulty {
    BEGINNER, INTERMEDIATE, ADVANCED
}