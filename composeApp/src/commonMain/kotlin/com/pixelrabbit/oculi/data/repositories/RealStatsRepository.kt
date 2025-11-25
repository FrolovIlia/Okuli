package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.data.db.RealmManager
import com.pixelrabbit.oculi.data.models.RealmExerciseCompletion
import com.pixelrabbit.oculi.data.models.RealmUserProgress
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.domain.models.UserProgress
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.*
// Удалены неиспользуемые и JVM-специфичные импорты (java.time, ChronoUnit)

class RealStatsRepository : StatsRepository {

    private val realm = RealmManager.getRealm()
    private val userId = "default_user"

    private val zone: TimeZone
        get() = TimeZone.currentSystemDefault()

    // Вспомогательная функция для преобразования Long в KMP-совместимый LocalDate
    private fun Long.toLocalDate(): kotlinx.datetime.LocalDate {
        return Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(zone)
            .date
    }

    // Вспомогательная функция для проверки, наступил ли новый день
    private fun isNewDay(lastDateMs: Long, nowMs: Long): Boolean {
        if (lastDateMs == 0L) return true
        val lastDate = lastDateMs.toLocalDate()
        val nowDate = nowMs.toLocalDate()
        // Используем прямой оператор сравнения для kotlinx.datetime.LocalDate
        return nowDate > lastDate
    }

    // Вычисляет, сколько полных дней прошло между датами (для проверки пропуска серии)
    private fun daysPassed(lastDateMs: Long, nowDate: kotlinx.datetime.LocalDate): Long {
        if (lastDateMs == 0L) return 0L
        val lastDate = lastDateMs.toLocalDate()
        // Используем KMP-совместимый метод until
        return lastDate.until(nowDate, DateTimeUnit.DAY).toLong()
    }

    override suspend fun getUserProgress(): UserProgress {
        val nowMs = Clock.System.now().toEpochMilliseconds()

        return realm.write {
            val p = query<RealmUserProgress>("userId == $0", userId).first().find()
                ?: throw IllegalStateException("User progress should always exist")

            val lastActiveDateMs = p.lastActivityDate

            // --- КЛЮЧЕВАЯ ЛОГИКА: СБРОС ЕЖЕДНЕВНОЙ СТАТИСТИКИ ПРИ ОТКРЫТИИ ---
            if (isNewDay(lastActiveDateMs, nowMs)) {

                val todayDate = nowMs.toLocalDate()
                // Используем KMP-совместимый daysPassed
                val daysSkipped = daysPassed(lastActiveDateMs, todayDate)

                // 1. Сброс ежедневных упражнений (сегодняшний счетчик)
                p.todayExercises = 0

                // 2. Корректировка серии, если пропуск более 1 дня
                if (daysSkipped > 1L) {
                    p.currentStreak = 0
                }
            }
            // --------------------------------------------------------------------

            // Возвращаем скорректированный прогресс
            UserProgress(
                totalExercises = p.totalExercises,
                totalTime = p.totalTime,
                currentStreak = p.currentStreak,
                todayExercises = p.todayExercises,
                lastActivityDate = p.lastActivityDate
            )
        }
    }

    override fun getUserProgressFlow(): Flow<UserProgress> {
        return realm
            .query<RealmUserProgress>("userId == $0", userId)
            .asFlow()
            .map { change ->
                val obj = change.list.firstOrNull()
                    ?: throw IllegalStateException("User progress should always exist")

                UserProgress(
                    totalExercises = obj.totalExercises,
                    totalTime = obj.totalTime,
                    currentStreak = obj.currentStreak,
                    todayExercises = obj.todayExercises,
                    lastActivityDate = obj.lastActivityDate
                )
            }
    }

    override suspend fun addExerciseCompletion(
        exerciseId: String,
        exerciseName: String,
        duration: Int,
        difficulty: String,
        successRate: Float
    ) {
        println("DEBUG: RealStatsRepository - Saving completion for $exerciseId")

        realm.write {
            // Сохраняем completion
            copyToRealm(RealmExerciseCompletion().apply {
                this.userId = this@RealStatsRepository.userId
                this.exerciseId = exerciseId
                this.exerciseName = exerciseName
                this.duration = duration
                this.difficulty = difficulty
                this.successRate = successRate
                this.completedAt = Clock.System.now().toEpochMilliseconds()
            })

            // Получаем прогресс
            val p = query<RealmUserProgress>("userId == $0", userId).first().find()
                ?: throw IllegalStateException("Progress should always exist")

            val now = Clock.System.now().toEpochMilliseconds()
            val todayDate = now.toLocalDate()
            val lastActiveDate = p.lastActivityDate.toLocalDate()
            val yesterdayDate = todayDate.minus(1, DateTimeUnit.DAY)

            val minutes = duration / 60

            p.totalExercises += 1
            p.totalTime += minutes

            // Логика обновления серии
            when {
                // Активность в тот же день
                todayDate == lastActiveDate -> p.todayExercises += 1

                // Активность на следующий день (серия продолжается).
                // Исправлено: заменено .equals() на ==
                lastActiveDate == yesterdayDate -> {
                    p.currentStreak += 1
                    p.todayExercises = 1
                }

                // Прошло больше одного дня или это первая активность.
                else -> {
                    p.currentStreak = 1
                    p.todayExercises = 1
                }
            }

            p.lastActivityDate = now
            p.updatedAt = now

            println("DEBUG: RealStatsRepository - Progress UPDATED: total=${p.totalExercises}, time=${p.totalTime}")
        }

        // Проверяем достижения
        ServiceLocator.achievementRepository().checkAndUnlockAchievements()
    }

    override suspend fun resetDailyStats() {
        realm.write {
            val p = query<RealmUserProgress>("userId == $0", userId).first().find()
                ?: throw IllegalStateException("Progress should always exist")
            p.todayExercises = 0
            p.updatedAt = Clock.System.now().toEpochMilliseconds()
        }
    }
}