package com.pixelrabbit.oculi.data.repositories

import com.pixelrabbit.oculi.data.db.RealmManager
import com.pixelrabbit.oculi.data.models.RealmUserProgress
import com.pixelrabbit.oculi.data.models.RealmExerciseCompletion
import com.pixelrabbit.oculi.di.ServiceLocator
import com.pixelrabbit.oculi.domain.models.UserProgress
import com.pixelrabbit.oculi.domain.repositories.StatsRepository
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.Instant

class RealStatsRepository : StatsRepository {
    private val realm = RealmManager.getRealm()
    private val userId = "default_user"

    private val zone: TimeZone
        get() = TimeZone.currentSystemDefault()

    override suspend fun getUserProgress(): UserProgress {
        // 🔥 Ленивая проверка streak при чтении
        checkAndResetStreakIfNecessary()

        val realmProgress = getOrCreateUserProgress()
        return UserProgress(
            totalExercises = realmProgress.totalExercises,
            totalTime = realmProgress.totalTime,
            currentStreak = realmProgress.currentStreak,
            todayExercises = realmProgress.todayExercises
        )
    }

    override fun getUserProgressFlow(): Flow<UserProgress> {
        // Важно: ленивая проверка streak вызывается перед подпиской на Flow (в UI)
        return realm.query<RealmUserProgress>("userId == $0", userId)
            .asFlow()
            .map { results ->
                val progress = results.list.firstOrNull() ?: getOrCreateUserProgress()
                UserProgress(
                    totalExercises = progress.totalExercises,
                    totalTime = progress.totalTime,
                    currentStreak = progress.currentStreak,
                    todayExercises = progress.todayExercises
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
            // Добавляем запись о выполнении
            copyToRealm(RealmExerciseCompletion().apply {
                this.userId = this@RealStatsRepository.userId
                this.exerciseId = exerciseId
                this.exerciseName = exerciseName
                this.duration = duration
                this.difficulty = difficulty
                this.successRate = successRate
                this.completedAt = Clock.System.now().toEpochMilliseconds()
            })

            // Обновляем общий прогресс
            val progress = query<RealmUserProgress>("userId == $0", userId)
                .first()
                .find()

            val currentTime = Clock.System.now().toEpochMilliseconds()
            val durationInMinutes = duration / 60

            if (progress == null) {
                // Создаем новый прогресс
                copyToRealm(RealmUserProgress().apply {
                    this.userId = this@RealStatsRepository.userId
                    this.totalExercises = 1
                    this.totalTime = durationInMinutes.toLong()
                    this.todayExercises = 1
                    this.currentStreak = 1
                    this.lastActivityDate = currentTime
                    this.createdAt = currentTime
                    this.updatedAt = currentTime
                })
            } else {
                val latestProgress = findLatest(progress) ?: return@write
                latestProgress.updateProgressInternal(durationInMinutes, currentTime)
            }
        }

        ServiceLocator.achievementRepository().checkAndUnlockAchievements()
    }

    override suspend fun resetDailyStats() {
        realm.write {
            val progress = query<RealmUserProgress>("userId == $0", userId)
                .first()
                .find()

            progress?.let { p ->
                val latestProgress = findLatest(p)
                latestProgress?.todayExercises = 0
                latestProgress?.updatedAt = Clock.System.now().toEpochMilliseconds()
            }
        }
    }

    private suspend fun getOrCreateUserProgress(): RealmUserProgress {
        return realm.query<RealmUserProgress>("userId == $0", userId)
            .first()
            .find() ?: createUserProgress()
    }

    private suspend fun createUserProgress(): RealmUserProgress {
        return realm.write {
            val now = Clock.System.now().toEpochMilliseconds()
            val newProgress = RealmUserProgress().apply {
                this.userId = this@RealStatsRepository.userId
                this.createdAt = now
                this.updatedAt = now
                this.lastActivityDate = now
            }
            copyToRealm(newProgress)
            newProgress
        }
    }

    // 🔥 Ленивая проверка обнуления streak
    private suspend fun checkAndResetStreakIfNecessary() {
        val realmProgress = getOrCreateUserProgress()

        if (realmProgress.currentStreak == 0 || realmProgress.lastActivityDate == 0L) return

        val lastActivityTime = realmProgress.lastActivityDate

        // Используем локальную дату
        val today = Clock.System.now().toLocalDateTime(zone).date
        val lastActivityDate = Instant.fromEpochMilliseconds(lastActivityTime)
            .toLocalDateTime(zone).date

        val yesterday = today.minus(1, DateTimeUnit.DAY)

        // Если последняя активность была позавчера или раньше → streak = 0
        if (lastActivityDate < yesterday) {
            realm.write {
                val latest = findLatest(realmProgress) ?: return@write
                latest.currentStreak = 0
                latest.updatedAt = Clock.System.now().toEpochMilliseconds()
            }
        }
    }

    // 🔥 Логика обновления прогресса при выполнении упражнения
    private fun RealmUserProgress.updateProgressInternal(
        durationInMinutes: Int,
        currentTime: Long
    ) {
        this.totalExercises += 1
        this.totalTime += durationInMinutes.toLong()

        val today = Clock.System.now().toLocalDateTime(zone).date

        val lastActivityDate = if (this.lastActivityDate > 0L) {
            Instant.fromEpochMilliseconds(this.lastActivityDate)
                .toLocalDateTime(zone).date
        } else {
            today.minus(2, DateTimeUnit.DAY)
        }

        if (today == lastActivityDate) {
            // Уже была активность сегодня
            this.todayExercises += 1
        } else {
            // Первая активность за день
            this.todayExercises = 1

            val yesterday = today.minus(1, DateTimeUnit.DAY)

            if (lastActivityDate == yesterday) {
                // Продолжение streak
                this.currentStreak += 1
            } else {
                // Новый streak
                this.currentStreak = 1
            }
        }

        this.lastActivityDate = currentTime
        this.updatedAt = currentTime
    }
}
