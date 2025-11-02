package com.pixelrabbit.oculi.domain.repositories

import androidx.compose.ui.input.key.Key.Companion.R
import com.pixelrabbit.oculi.domain.models.Difficulty
import com.pixelrabbit.oculi.domain.models.Exercise
import com.pixelrabbit.oculi.domain.models.ExerciseType

class ExerciseRepositoryImpl : ExerciseRepository {

    // Временные данные - позже заменим на реальные из базы
    private val exercises = listOf(
        Exercise(
            id = "follow_target",
            title = "Преследуй цель",
            description = "Изображение объектов для расслабления глазных мышц",
            duration = 120,
            type = ExerciseType.ACCOMMODATION,
            difficulty = Difficulty.BEGINNER,
            isPremium = false,
            icon = "🎯",
            instructions = listOf(
                "Сядьте удобно",
                "Расслабьтесь и закройте глаза",
                "Двигайте ими, изображая разные фигуры",
                "Повторяйте 10-12 раз"
            ),
            imageName = "sledovanie_za"
        ),
        Exercise(
            id = "focus_shift",
            title = "Фокус-сдвиг",
            description = "Чередование ближнего и дальнего зрения для снятия цифрового напряжения",
            duration = 180,
            type = ExerciseType.ACCOMMODATION,
            difficulty = Difficulty.BEGINNER,
            isPremium = false,
            icon = "🔍",
            instructions = listOf(
                "Держите палец на расстоянии вытянутой руки от глаз",
                "Не двигайте головой",
                "Следите за кончиком пальца \nприближая и удаляя его от кончика носа",
                "Повторяйте 10-15 раз"
            ),
            imageName = "focus"
        ),
        Exercise(
            id = "palming",
            title = "Пальминг",
            description = "Техника полного расслабления глаз через затемнение",
            duration = 300,
            type = ExerciseType.RELAXATION,
            difficulty = Difficulty.BEGINNER,
            isPremium = true,
            icon = "👐",
            instructions = listOf(
                "Разотрите ладони до тепла",
                "Закройте глаза ладонями без давления",
                "Представьте абсолютную темноту",
                "Дышите глубоко и расслабленно"
            ),
            imageName = "palming"
        ),
        Exercise(
            id = "figure_eight",
            title = "Восьмерки",
            description = "Движение глаз по траектории восьмерки для улучшения подвижности",
            duration = 120,
            type = ExerciseType.MOBILITY,
            difficulty = Difficulty.INTERMEDIATE,
            isPremium = true,
            icon = "∞",
            instructions = listOf(
                "Представьте большую восьмерку перед собой",
                "Медленно обводите ее контур глазами",
                "Сначала по часовой стрелке, затем против",
                "Не двигайте головой"
            ),
            imageName = "vosmerka"
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