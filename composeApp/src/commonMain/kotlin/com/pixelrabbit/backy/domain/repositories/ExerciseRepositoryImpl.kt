package com.pixelrabbit.backy.domain.repositories

import com.pixelrabbit.backy.domain.models.Difficulty
import com.pixelrabbit.backy.domain.models.Exercise
import com.pixelrabbit.backy.domain.models.ExerciseType

class ExerciseRepositoryImpl : ExerciseRepository {

    private val exercises = listOf(
        Exercise(
            id = "neck_tilts",
            title = "Наклоны шеи",
            description = "Мягкая растяжка боковых мышц шеи",
            duration = 90,
            type = ExerciseType.MOBILITY,
            difficulty = Difficulty.BEGINNER,
            icon = "↔️",
            instructions = listOf(
                "Сядьте прямо",
                "Медленно наклоните голову к правому плечу",
                "Задержитесь 3 секунды",
                "Вернитесь в центр",
                "Повторите влево",
                "Сделайте 8–10 повторений"
            ),
            imageName = "neck_tilts"
        ),

        Exercise(
            id = "neck_rotation",
            title = "Повороты шеи",
            description = "Улучшает подвижность шейного отдела",
            duration = 90,
            type = ExerciseType.MOBILITY,
            difficulty = Difficulty.BEGINNER,
            icon = "🔄",
            instructions = listOf(
                "Сядьте ровно",
                "Поверните голову вправо",
                "Вернитесь в центр",
                "Поверните голову влево",
                "Сделайте 8–10 повторений"
            ),
            imageName = "neck_rotation"
        ),

        Exercise(
            id = "shoulder_rolls",
            title = "Круги плечами",
            description = "Снимает напряжение в плечах и верхней части спины",
            duration = 90,
            type = ExerciseType.RELAXATION,
            difficulty = Difficulty.BEGINNER,
            icon = "🌀",
            instructions = listOf(
                "Опустите руки вдоль тела",
                "Поднимите плечи вверх",
                "Отведите назад и опустите",
                "Сделайте 10 кругов назад, затем 10 вперёд"
            ),
            imageName = "shoulder_rolls"
        ),

        Exercise(
            id = "shoulder_blades",
            title = "Сведение лопаток",
            description = "Укрепляет мышцы верхней части спины",
            duration = 120,
            type = ExerciseType.STRENGTH,
            difficulty = Difficulty.BEGINNER,
            icon = "🏹",
            instructions = listOf(
                "Сядьте или встаньте прямо",
                "Отведите плечи назад",
                "Сведите лопатки вместе",
                "Удерживайте 5 секунд",
                "Повторите 10 раз"
            ),
            imageName = "shoulder_blades"
        ),

        Exercise(
            id = "chin_tuck",
            title = "Подбородок назад",
            description = "Исправляет положение головы при работе за компьютером",
            duration = 90,
            type = ExerciseType.STRENGTH,
            difficulty = Difficulty.BEGINNER,
            icon = "🧍",
            instructions = listOf(
                "Сядьте прямо",
                "Смотрите вперед",
                "Медленно отведите подбородок назад",
                "Удерживайте 5 секунд",
                "Повторите 10 раз"
            ),
            imageName = "chin_tuck"
        ),

        Exercise(
            id = "cat_cow",
            title = "Кошка-корова",
            description = "Мягкая мобилизация позвоночника",
            duration = 120,
            type = ExerciseType.MOBILITY,
            difficulty = Difficulty.INTERMEDIATE,
            icon = "🐈",
            instructions = listOf(
                "Встаньте на четвереньки",
                "На вдохе прогните спину и поднимите голову",
                "На выдохе округлите спину и опустите голову",
                "Повторите 10–12 раз"
            ),
            imageName = "cat_cow"
        ),

        Exercise(
            id = "side_stretch",
            title = "Боковая растяжка",
            description = "Растяжка боковых мышц спины",
            duration = 90,
            type = ExerciseType.STRETCH,
            difficulty = Difficulty.BEGINNER,
            icon = "🌿",
            instructions = listOf(
                "Поднимите правую руку вверх",
                "Наклонитесь влево",
                "Задержитесь 5 секунд",
                "Повторите на другую сторону",
                "Сделайте 8 повторений"
            ),
            imageName = "side_stretch"
        ),

        Exercise(
            id = "desk_shoulder_open",
            title = "Открытие плеч за столом",
            description = "Снимает напряжение плеч при сидячей работе",
            duration = 60,
            type = ExerciseType.RELAXATION,
            difficulty = Difficulty.BEGINNER,
            icon = "💺",
            instructions = listOf(
                "Сидя, сцепите руки за спиной",
                "Разведите плечи назад",
                "Задержитесь 5 секунд",
                "Повторите 5–7 раз"
            ),
            imageName = "desk_shoulder_open"
        ),

        Exercise(
            id = "thoracic_twist",
            title = "Повороты верхнего отдела спины",
            description = "Улучшает подвижность грудного отдела",
            duration = 90,
            type = ExerciseType.MOBILITY,
            difficulty = Difficulty.INTERMEDIATE,
            icon = "🔄",
            instructions = listOf(
                "Сядьте прямо",
                "Поверните корпус вправо, держа руки перед собой",
                "Задержитесь 3 секунды",
                "Поверните влево",
                "Повторите 8–10 раз"
            ),
            imageName = "thoracic_twist"
        ),

        Exercise(
            id = "trap_stretch",
            title = "Растяжка трапеций",
            description = "Снимает напряжение шеи и плеч",
            duration = 60,
            type = ExerciseType.STRETCH,
            difficulty = Difficulty.BEGINNER,
            icon = "💆",
            instructions = listOf(
                "Наклоните голову к правому плечу",
                "Помогайте рукой слегка надавливая",
                "Задержитесь 5 секунд",
                "Повторите на другую сторону",
                "Сделайте 2–3 подхода"
            ),
            imageName = "trap_stretch"
        )
    )

    override suspend fun getAllExercises(): List<Exercise> = exercises

    override suspend fun getExerciseById(id: String): Exercise? =
        exercises.find { it.id == id }

    override suspend fun getExercisesByType(type: ExerciseType): List<Exercise> =
        exercises.filter { it.type == type }

    override suspend fun getFreeExercises(): List<Exercise> =
        exercises.filter { !it.isPremium }

    override suspend fun markExerciseCompleted(exerciseId: String, duration: Int) {
        println("Упражнение $exerciseId завершено за $duration секунд")
    }
}