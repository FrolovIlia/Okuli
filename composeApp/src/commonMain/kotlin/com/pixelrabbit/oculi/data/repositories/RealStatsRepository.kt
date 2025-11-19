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
        // 🔥 КМР ПРАКТИКА: Ленивый сброс до 0 при ЧТЕНИИ статистики
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
        // NOTE: Flow не может вызывать suspend функцию. Для гарантии сброса
        // разработчик должен вызвать checkAndResetStreakIfNecessary() перед началом
        // подписки на этот Flow в UseCase или ViewModel.
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

                // 🔥 ИСПОЛЬЗУЕМ ЛОГИКУ ТРАНЗАКЦИИ
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

    // 🔥 НОВЫЙ МЕТОД: Ленивый сброс серии до 0 (для чтения)
    private suspend fun checkAndResetStreakIfNecessary() {
        val realmProgress = getOrCreateUserProgress()

        // Сброс не нужен, если серия уже 0 или дата активности не установлена
        if (realmProgress.currentStreak == 0 || realmProgress.lastActivityDate == 0L) return

        val lastActivityTime = realmProgress.lastActivityDate

        // 🔥 ПРАКТИКА UTC: Сравниваем даты в UTC для надежности
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val lastActivityDate = Instant.fromEpochMilliseconds(lastActivityTime)
            .toLocalDateTime(TimeZone.UTC).date

        val yesterday = today.minus(1, DateTimeUnit.DAY)

        // Если последняя активность была позавчера или раньше, сбрасываем серию до 0
        if (lastActivityDate < yesterday) {
            realm.write {
                val latestProgress = findLatest(realmProgress) ?: return@write
                // Сброс серии до 0 (состояние, которое увидит пользователь до начала тренировки)
                latestProgress.currentStreak = 0
                latestProgress.updatedAt = Clock.System.now().toEpochMilliseconds()
                println("DEBUG: Streak reset to 0 by KMP Lazy Reset.")
            }
        }
    }

    // 🔥 ЧИСТАЯ ЛОГИКА ТРАНЗАКЦИИ: Обрабатывает только результат выполнения упражнения
    private fun RealmUserProgress.updateProgressInternal(
        durationInMinutes: Int,
        currentTime: Long
    ) {
        this.totalExercises += 1
        this.totalTime += durationInMinutes.toLong()

        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date

        val lastActivityDate = if (this.lastActivityDate > 0L) {
            Instant.fromEpochMilliseconds(this.lastActivityDate)
                .toLocalDateTime(TimeZone.UTC).date
        } else {
            today.minus(2, DateTimeUnit.DAY)
        }

        if (today == lastActivityDate) {
            // Активность уже была сегодня
            this.todayExercises += 1
        } else {
            // Активность сегодня первая
            this.todayExercises = 1

            val yesterday = today.minus(1, DateTimeUnit.DAY)

            if (lastActivityDate == yesterday) {
                // СЦЕНАРИЙ 1: Продолжение серии
                this.currentStreak += 1
            } else {
                // СЦЕНАРИЙ 2: Пропуск дня (серия была сброшена до 0 или была > 0, неважно).
                // Начинаем новую серию с 1.
                this.currentStreak = 1
            }
        }

        this.lastActivityDate = currentTime
        this.updatedAt = currentTime
    }
}