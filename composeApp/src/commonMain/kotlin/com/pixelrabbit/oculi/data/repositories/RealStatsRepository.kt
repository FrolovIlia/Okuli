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

    private fun Long.toLocalDate() =
        Instant.fromEpochMilliseconds(this).toLocalDateTime(zone).date

    override suspend fun getUserProgress(): UserProgress {
        // Прогресс гарантированно существует благодаря RealmManager
        val p = realm.query<RealmUserProgress>("userId == $0", userId).first().find()
            ?: throw IllegalStateException("User progress should always exist")

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
            .map { change ->
                val obj = change.list.firstOrNull()
                    ?: throw IllegalStateException("User progress should always exist")

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

            // Получаем прогресс (он ГАРАНТИРОВАННО существует)
            val p = query<RealmUserProgress>("userId == $0", userId).first().find()
                ?: throw IllegalStateException("Progress should always exist")

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