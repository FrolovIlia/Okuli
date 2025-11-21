package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.data.db.RealmManager
import com.pixelrabbit.oculi.data.models.RealmUserProgress
import com.pixelrabbit.oculi.data.models.RealmExerciseCompletion
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.domain.models.UserProgress
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.*

class RealStatsRepository : StatsRepository {

    private val realm = RealmManager.getRealm()
    private val userId = "default_user"

    private val zone: TimeZone
        get() = TimeZone.currentSystemDefault()

    // ----------------------------------------------------
    // Helpers
    // ----------------------------------------------------

    private suspend fun getOrCreateProgress(): RealmUserProgress {
        val existing =
            realm.query<RealmUserProgress>("userId == $0", userId).first().find()

        if (existing != null) return existing

        return realm.write {
            val now = Clock.System.now().toEpochMilliseconds()

            val obj = RealmUserProgress().apply {
                this.userId = this@RealStatsRepository.userId
                this.totalExercises = 0
                this.totalTime = 0
                this.currentStreak = 0
                this.todayExercises = 0
                this.lastActivityDate = now
                this.createdAt = now
                this.updatedAt = now
            }

            copyToRealm(obj)
        }
    }

    private fun Long.toLocalDate(): LocalDate =
        Instant.fromEpochMilliseconds(this).toLocalDateTime(zone).date

    private suspend fun checkAndResetStreakIfNeeded() {
        val p = getOrCreateProgress()

        if (p.currentStreak == 0) return

        val today = Clock.System.now().toEpochMilliseconds().toLocalDate()
        val lastActive = p.lastActivityDate.toLocalDate()

        val yesterday = today.minus(1, DateTimeUnit.DAY)

        if (lastActive < yesterday) {
            realm.write {
                val obj = findProgress() ?: return@write
                obj.currentStreak = 0
                obj.todayExercises = 0
                obj.updatedAt = Clock.System.now().toEpochMilliseconds()
            }
        }
    }

    private fun findProgress(): RealmUserProgress? =
        realm.query<RealmUserProgress>("userId == $0", userId).first().find()

    // ----------------------------------------------------
    // Public API
    // ----------------------------------------------------

    override suspend fun getUserProgress(): UserProgress {
        checkAndResetStreakIfNeeded()

        val p = getOrCreateProgress()

        return UserProgress(
            totalExercises = p.totalExercises,
            totalTime = p.totalTime,
            currentStreak = p.currentStreak,
            todayExercises = p.todayExercises
        )
    }

    override fun getUserProgressFlow(): Flow<UserProgress> {
        return realm
            .query<RealmUserProgress>("userId == $0", userId)
            .asFlow()
            .map { change: ResultsChange<RealmUserProgress> ->

                val obj = change.list.firstOrNull()
                    ?: return@map UserProgress() // безопасное значение

                UserProgress(
                    totalExercises = obj.totalExercises,
                    totalTime = obj.totalTime,
                    currentStreak = obj.currentStreak,
                    todayExercises = obj.todayExercises
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
        realm.write {
            // Сохраняем выполненное упражнение
            copyToRealm(
                RealmExerciseCompletion().apply {
                    this.userId = this@RealStatsRepository.userId
                    this.exerciseId = exerciseId
                    this.exerciseName = exerciseName
                    this.duration = duration
                    this.difficulty = difficulty
                    this.successRate = successRate
                    this.completedAt = Clock.System.now().toEpochMilliseconds()
                }
            )

            // Обновляем прогресс
            val p = findProgress() ?: return@write

            val now = Clock.System.now().toEpochMilliseconds()
            val today = now.toLocalDate()
            val lastActive = p.lastActivityDate.toLocalDate()
            val yesterday = today.minus(1, DateTimeUnit.DAY)

            val minutes = duration / 60

            p.totalExercises += 1
            p.totalTime += minutes

            when {
                today == lastActive -> {
                    p.todayExercises += 1
                }

                lastActive == yesterday -> {
                    p.currentStreak += 1
                    p.todayExercises = 1
                }

                else -> {
                    p.currentStreak = 1
                    p.todayExercises = 1
                }
            }

            p.lastActivityDate = now
            p.updatedAt = now
        }

        // Проверяем достижения
        ServiceLocator.achievementRepository().checkAndUnlockAchievements()
    }

    override suspend fun resetDailyStats() {
        realm.write {
            val p = findProgress() ?: return@write

            p.todayExercises = 0
            p.updatedAt = Clock.System.now().toEpochMilliseconds()
        }
    }
}
