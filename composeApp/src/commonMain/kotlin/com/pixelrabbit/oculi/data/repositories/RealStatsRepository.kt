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

    override suspend fun getUserProgress(): UserProgress {
        val realmProgress = getOrCreateUserProgress()
        return UserProgress(
            totalExercises = realmProgress.totalExercises,
            totalTime = realmProgress.totalTime,
            currentStreak = realmProgress.currentStreak,
            todayExercises = realmProgress.todayExercises
        )
    }

    override fun getUserProgressFlow(): Flow<UserProgress> {
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
        println("DEBUG: Starting addExerciseCompletion for $exerciseName")

        realm.write {
            // Добавляем запись о выполнении
            copyToRealm(RealmExerciseCompletion().apply {
                this.userId = this@RealStatsRepository.userId
                this.exerciseId = exerciseId
                this.exerciseName = exerciseName
                this.duration = duration // сохраняем в секундах для истории
                this.difficulty = difficulty
                this.successRate = successRate
                this.completedAt = Clock.System.now().toEpochMilliseconds()
            })

            // Обновляем общий прогресс
            val progress = query<RealmUserProgress>("userId == $0", userId)
                .first()
                .find()

            val currentTime = Clock.System.now().toEpochMilliseconds()
            val durationInMinutes = duration / 60 // КОНВЕРТИРУЕМ В МИНУТЫ

            if (progress == null) {
                // Создаем новый прогресс если его нет
                copyToRealm(RealmUserProgress().apply {
                    this.userId = this@RealStatsRepository.userId
                    this.totalExercises = 1
                    this.totalTime = durationInMinutes.toLong() // сохраняем в минутах
                    this.todayExercises = 1
                    this.currentStreak = 1
                    this.lastActivityDate = currentTime
                    this.createdAt = currentTime
                    this.updatedAt = currentTime
                })
                println("DEBUG: Created new progress")
            } else {
                val latestProgress = findLatest(progress) ?: return@write

                latestProgress.totalExercises += 1
                latestProgress.totalTime += durationInMinutes // добавляем в минутах

                // Обновляем ежедневную статистику и серию
                val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val lastActivityDate = if (latestProgress.lastActivityDate > 0L) {
                    Instant.fromEpochMilliseconds(latestProgress.lastActivityDate)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date
                } else {
                    // Если дата не установлена, считаем что активности не было
                    today.minus(2, DateTimeUnit.DAY)
                }

                if (today == lastActivityDate) {
                    latestProgress.todayExercises += 1
                } else {
                    latestProgress.todayExercises = 1
                    // Обновляем серию
                    val yesterday = today.minus(1, DateTimeUnit.DAY)
                    if (lastActivityDate == yesterday) {
                        latestProgress.currentStreak += 1
                    } else if (lastActivityDate < yesterday) {
                        latestProgress.currentStreak = 1
                    }
                }

                latestProgress.lastActivityDate = currentTime
                latestProgress.updatedAt = currentTime
                println("DEBUG: Updated progress - exercises: ${latestProgress.totalExercises}, time: ${latestProgress.totalTime}")
            }
        }

        println("DEBUG: Exercise saved, checking achievements...")
        ServiceLocator.achievementRepository().checkAndUnlockAchievements()
        println("DEBUG: Achievements checked")
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
            val newProgress = RealmUserProgress().apply {
                this.userId = this@RealStatsRepository.userId
                this.createdAt = Clock.System.now().toEpochMilliseconds()
                this.updatedAt = Clock.System.now().toEpochMilliseconds()
                this.lastActivityDate = Clock.System.now().toEpochMilliseconds()
            }
            copyToRealm(newProgress)
            newProgress
        }
    }
}