package com.pixelrabbit.backy.domain.models

data class Exercise(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int,
    val type: ExerciseType,
    val difficulty: Difficulty,
    val isPremium: Boolean = false,
    val icon: String,
    val instructions: List<String>,
    val imageName: String? = null
)

enum class ExerciseType {

    // Подвижность суставов
    MOBILITY,

    // Растяжка мышц
    STRETCH,

    // Укрепление мышц
    STRENGTH,

    // Расслабление
    RELAXATION
}

enum class Difficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}