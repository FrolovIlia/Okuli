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
    val instructions: List<String>
)

enum class ExerciseType {
    ACCOMMODATION,    // Тренировка аккомодации
    RELAXATION,       // Расслабление
    MOBILITY,         // Подвижность глаз
    DIGITAL_RELIEF    // Снятие цифрового напряжения
}

enum class Difficulty {
    BEGINNER, INTERMEDIATE, ADVANCED
}