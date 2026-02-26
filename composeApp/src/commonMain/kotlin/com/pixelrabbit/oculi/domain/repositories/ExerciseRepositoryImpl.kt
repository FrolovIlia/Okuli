package com.pixelrabbit.oculi.domain.repositories

import com.pixelrabbit.oculi.domain.models.Difficulty
import com.pixelrabbit.oculi.domain.models.Exercise
import com.pixelrabbit.oculi.domain.models.ExerciseType

class ExerciseRepositoryImpl : ExerciseRepository {

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
                "Повторяйте медленно, по 10-12 раз в каждую сторону"
            ),
            imageName = "sledovanie_za"
        ),
        Exercise(
            id = "focus_shift",
            title = "Фокус-сдвиг",
            description = "Чередование ближнего и дальнего зрения для снятия цифрового напряжения",
            duration = 120,
            type = ExerciseType.ACCOMMODATION,
            difficulty = Difficulty.BEGINNER,
            isPremium = false,
            icon = "🔍",
            instructions = listOf(
                "Держите палец на расстоянии вытянутой руки от глаз",
                "Не двигайте головой",
                "Следите за кончиком пальца приближая и удаляя его от кончика носа",
                "Повторяйте медленно 10-15 раз"
            ),
            imageName = "focus"
        ),
        Exercise(
            id = "palming",
            title = "Пальминг",
            description = "Техника полного расслабления глаз через затемнение",
            duration = 180,
            type = ExerciseType.RELAXATION,
            difficulty = Difficulty.BEGINNER,
            isPremium = false,
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
            duration = 90,
            type = ExerciseType.MOBILITY,
            difficulty = Difficulty.INTERMEDIATE,
            isPremium = false,
            icon = "∞",
            instructions = listOf(
                "Представьте большую восьмерку перед собой",
                "Медленно обводите ее контур глазами",
                "Сначала по часовой стрелке, затем против",
                "Не двигайте головой"
            ),
            imageName = "vosmerka"
        ),

        Exercise(
            id = "window_mark",
            title = "Блики на стекле",
            description = "Переключение фокуса с ближней метки на дальний объект для снятия спазма аккомодации",
            duration = 180,
            type = ExerciseType.ACCOMMODATION,
            difficulty = Difficulty.BEGINNER,
            isPremium = false,
            icon = "🪟",
            instructions = listOf(
                "Найдите окно с видом на улицу",
                "На стекле на уровне глаз сделайте небольшую метку",
                "Встаньте в 30-50 см от окна",
                "Сфокусируйтесь на метке 2-3 секунды",
                "Переведите взгляд на удаленный объект за окном",
                "Рассматривайте детали 5-10 секунд",
                "Повторите цикл 10-15 раз"
            ),
            imageName = "window_mark"
        ),
        Exercise(
            id = "squeeze_eyes",
            title = "Жмурки",
            description = "Интенсивное зажмуривание для улучшения кровообращения в глазных мышцах",
            duration = 90,
            type = ExerciseType.RELAXATION,
            difficulty = Difficulty.BEGINNER,
            isPremium = false,
            icon = "😌",
            instructions = listOf(
                "Сядьте прямо, расслабьте плечи",
                "Максимально сильно зажмурьте глаза на 3-5 секунд",
                "Ощутите напряжение мышц вокруг глаз",
                "Широко и расслабленно откройте глаза на 3-5 секунд",
                "Повторите 7-10 раз",
                "Завершите легким морганием"
            ),
            imageName = "squeeze_eyes"
        ),
        Exercise(
            id = "blinking",
            title = "Ритмичное моргание",
            description = "Восстановление правильного ритма моргания для увлажнения глаз",
            duration = 60,
            type = ExerciseType.RELAXATION,
            difficulty = Difficulty.BEGINNER,
            isPremium = false,
            icon = "👁️",
            instructions = listOf(
                "Примите удобное положение сидя",
                "Расслабьте лицо и лоб",
                "Начните моргать легко и быстро",
                "Полностью смыкайте и размыкайте веки",
                "Представьте, что ресницы — крылья бабочки",
                "Дышите ровно и спокойно",
                "Проводите 30-60 секунд"
            ),
            imageName = "blinking"
        ),
        Exercise(
            id = "acupressure",
            title = "Массаж точек",
            description = "Снятие напряжения через воздействие на биологически активные точки",
            duration = 150,
            type = ExerciseType.RELAXATION,
            difficulty = Difficulty.INTERMEDIATE,
            isPremium = false,
            icon = "💆",
            instructions = listOf(
                "Разотрите руки до тепла",
                "Закройте глаза и расслабьтесь",
                "Найдите точки у внутренних уголков глаз у переносицы",
                "Круговыми движениями мягко массируйте 5-7 секунд",
                "Перейдите к точкам по центру под бровями",
                "Затем — у внешних уголков глаз",
                "Завершите точками на скулах под зрачками",
                "Дышите глубоко и ровно"
            ),
            imageName = "acupressure"
        ),
        Exercise(
            id = "peripheral_vision",
            title = "Слепая зона",
            description = "Тренировка периферического зрения без прямого фокуса",
            duration = 120,
            type = ExerciseType.MOBILITY,
            difficulty = Difficulty.INTERMEDIATE,
            isPremium = false,
            icon = "🌀",
            instructions = listOf(
                "Вытяните руку с поднятым большим пальцем",
                "Смотрите прямо перед собой на удаленную точку",
                "Фиксируйте центральный взгляд, не двигая глазами",
                "Боковым зрением следите за движением пальца",
                "Медленно двигайте руку вправо-влево",
                "Старайтесь удерживать фокус на дальнем объекте",
                "Повторите 10 раз в каждую сторону"
            ),
            imageName = "peripheral_vision"
        ),
        Exercise(
            id = "soft_solarization",
            title = "Мягкая соляризация",
            description = "Расслабление глаз через наблюдение за мерцающим пламенем свечи",
            duration = 240,
            type = ExerciseType.RELAXATION,
            difficulty = Difficulty.ADVANCED,
            isPremium = false,
            icon = "🕯️",
            instructions = listOf(
                "Затемните комнату и зажгите свечу",
                "Поставьте свечу на расстоянии 50-70 см",
                "Сядьте удобно, свеча на уровне глаз",
                "Мягко смотрите на пламя, часто моргая",
                "Не фокусируйтесь остро, наблюдайте рассеянно",
                "Через 1-2 минуту сядьте спиной к свече",
                "Выполните пальминг, представляя остаточное свечение"
            ),
            imageName = "soft_solarization"
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
        println("Упражнение $exerciseId завершено за $duration секунд")
    }
}