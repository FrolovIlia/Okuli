package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.domain.models.Exercise
import com.pixelrabbit.oculi.domain.models.ExerciseType
import com.pixelrabbit.oculi.domain.repositories.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl : ExerciseRepository {

    // Временные данные - позже заменим на реальные из базы
    private val exercises = listOf(
        Exercise(
            id = "follow_target",
            title = "Преследуй цель",
            description = "Слежение за движущимся объектом для тренировки аккомодации глаз",
            duration = 120,
            type = ExerciseType.ACCOMMODATION,
            difficulty = com.pixelrabbit.oculi.domain.models.Difficulty.BEGINNER,
            isPremium = false,
            icon = "🎯",
            instructions = listOf(
                "Сядьте удобно перед экраном",
                "Следите за движущимся объектом глазами",
                "Не двигайте головой",
                "Выполняйте плавные движения глазами"
            )
        ),
        Exercise(
            id = "focus_shift",
            title = "Фокус-сдвиг",
            description = "Чередование ближнего и дальнего зрения для снятия цифрового напряжения",
            duration = 180,
            type = ExerciseType.ACCOMMODATION,
            difficulty = com.pixelrabbit.oculi.domain.models.Difficulty.BEGINNER,
            isPremium = false,
            icon = "🔍",
            instructions = listOf(
                "Держите палец на расстоянии 15-20 см от глаз",
                "Сфокусируйтесь на пальце 2 секунды",
                "Переведите взгляд на удаленный объект",
                "Повторяйте 10-15 раз"
            )
        ),
        Exercise(
            id = "palming",
            title = "Пальминг",
            description = "Техника полного расслабления глаз через затемнение",
            duration = 300,
            type = ExerciseType.RELAXATION,
            difficulty = com.pixelrabbit.oculi.domain.models.Difficulty.BEGINNER,
            isPremium = true,
            icon = "👐",
            instructions = listOf(
                "Разотрите ладони до тепла",
                "Закройте глаза ладонями без давления",
                "Представьте абсолютную темноту",
                "Дышите глубоко и расслабленно"
            )
        ),
        Exercise(
            id = "figure_eight",
            title = "Восьмерки",
            description = "Движение глаз по траектории восьмерки для улучшения подвижности",
            duration = 120,
            type = ExerciseType.MOBILITY,
            difficulty = com.pixelrabbit.oculi.domain.models.Difficulty.INTERMEDIATE,
            isPremium = true,
            icon = "∞",
            instructions = listOf(
                "Представьте большую восьмерку перед собой",
                "Медленно обводите ее контур глазами",
                "Сначала по часовой стрелке, затем против",
                "Не двигайте головой"
            )
        )
    )

    override suspend fun getAllExercises(): List<Exercise> {
        return exercises
    }

    override suspend fun getExerciseById(id: String): Exercise? {
        return exercises.find { it.id == id }
    }

    override suspend fun getExercisesByType(type: ExerciseType): List<Exercise> {
        return exercises.filter { it.type == type }
    }

    override suspend fun getFreeExercises(): List<Exercise> {
        return exercises.filter { !it.isPremium }
    }

    override suspend fun markExerciseCompleted(exerciseId: String, duration: Int) {
        // Пока просто логируем - позже добавим в базу
        println("Упражнение $exerciseId завершено за $duration секунд")
    }
}