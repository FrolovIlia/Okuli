package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.data.db.RealmManager
import com.pixelrabbit.oculi.data.models.RealmExerciseCompletion
import com.pixelrabbit.oculi.data.models.RealmUserProgress
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.domain.models.UserProgress
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class RealStatsRepository : StatsRepository {

    private val realm = RealmManager.getRealm()
    private val userId = "default_user"

    private val zone: TimeZone
        get() = TimeZone.currentSystemDefault()

    // ----------------------------------------------------
    // Helpers
    // ----------------------------------------------------

    private fun Long.toLocalDate() =
        Instant.fromEpochMilliseconds(this).toLocalDateTime(zone).date

    private suspend fun ensureProgressExists() {
        val existing = realm.query<RealmUserProgress>("userId == $0", userId).first().find()
        if (existing != null) return

        realm.write {
            val now = Clock.System.now().toEpochMilliseconds()

            copyToRealm(
                RealmUserProgress().apply {
                    this.userId = this@RealStatsRepository.userId
                    this.totalExercises = 0
                    this.totalTime = 0
                    this.currentStreak = 0
                    this.todayExercises = 0
                    this.lastActivityDate = now
                    this.createdAt = now
                    this.updatedAt = now
                }
            )
        }
    }

    private fun readProgressSnapshot(): UserProgress {
        val p = realm.query<RealmUserProgress>("userId == $0", userId).first().find()
            ?: return UserProgress()

        return UserProgress(
            totalExercises = p.totalExercises,
            totalTime = p.totalTime,
            currentStreak = p.currentStreak,
            todayExercises = p.todayExercises
        )
    }

    // ----------------------------------------------------
    // Public API
    // ----------------------------------------------------

    override suspend fun getUserProgress(): UserProgress {
        checkAndResetStreakIfNeeded()
        ensureProgressExists()
        return readProgressSnapshot()
    }

    override fun getUserProgressFlow(): Flow<UserProgress> {
        return realm
            .query<RealmUserProgress>("userId == $0", userId)
            .asFlow()
            .map { change: ResultsChange<RealmUserProgress> ->
                val obj = change.list.firstOrNull()
                    ?: return@map UserProgress()

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

            // Сохраняем запись
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

            // Получаем прогресс
            val p = query<RealmUserProgress>("userId == $0", userId).first().find()
                ?: copyToRealm(
                    RealmUserProgress().apply {
                        this.userId = this@RealStatsRepository.userId
                        val now = Clock.System.now().toEpochMilliseconds()
                        this.lastActivityDate = now
                        this.createdAt = now
                        this.updatedAt = now
                    }
                )

            val now = Clock.System.now().toEpochMilliseconds()
            val today = now.toLocalDate()
            val lastActive = p.lastActivityDate.toLocalDate()
            val yesterday = today.minus(1, DateTimeUnit.DAY)

            val minutes = duration / 60

            p.totalExercises += 1
            p.totalTime += minutes

            when {
                today == lastActive -> p.todayExercises += 1
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

        // вызываем после транзакции
        ServiceLocator.achievementRepository().checkAndUnlockAchievements()
    }

    override suspend fun resetDailyStats() {
        realm.write {
            val p = query<RealmUserProgress>("userId == $0", userId).first().find()
                ?: return@write

            p.todayExercises = 0
            p.updatedAt = Clock.System.now().toEpochMilliseconds()
        }
    }

    private suspend fun checkAndResetStreakIfNeeded() {
        realm.write {
            val p = query<RealmUserProgress>("userId == $0", userId).first().find()
                ?: return@write

            if (p.currentStreak == 0) return@write

            val now = Clock.System.now().toEpochMilliseconds()
            val today = now.toLocalDate()
            val lastActive = p.lastActivityDate.toLocalDate()
            val yesterday = today.minus(1, DateTimeUnit.DAY)

            if (lastActive < yesterday) {
                p.currentStreak = 0
                p.todayExercises = 0
                p.updatedAt = now
            }
        }
    }
}
