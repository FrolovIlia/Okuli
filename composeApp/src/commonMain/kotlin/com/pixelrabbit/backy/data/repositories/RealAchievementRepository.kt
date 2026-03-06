package com.pixelrabbit.backy.data.repositories

import com.pixelrabbit.backy.db.RealmManager
import com.pixelrabbit.backy.data.models.RealmAchievement
import com.pixelrabbit.backy.data.models.RealmShownAchievement
import com.pixelrabbit.backy.data.models.RealmUserProgress
import com.pixelrabbit.backy.domain.models.Achievement
import com.pixelrabbit.backy.domain.repositories.AchievementRepository
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class RealAchievementRepository : AchievementRepository {
    private val realm = RealmManager.getRealm()
    private val userId = "default_user"

    override suspend fun getAchievements(): List<Achievement> {
        val realmAchievements = realm.query<RealmAchievement>("userId == $0", userId)
            .find()

        println("DEBUG: Found ${realmAchievements.size} achievements in database")

        if (realmAchievements.isEmpty()) {
            println("DEBUG: No achievements found, creating initial ones")
            val achievements = createInitialAchievements()
            checkAndUnlockAchievements()
            return achievements
        }

        return realmAchievements.map { realmAchievement ->
            convertToDomainAchievement(realmAchievement)
        }
    }

    override fun getAchievementsFlow(): Flow<List<Achievement>> {
        return realm.query<RealmAchievement>("userId == $0", userId)
            .asFlow()
            .map { results ->
                results.list.map { realmAchievement ->
                    convertToDomainAchievement(realmAchievement)
                }
            }
    }

    override suspend fun checkAndUnlockAchievements() {
        println("DEBUG: Starting checkAndUnlockAchievements")

        val progress = realm.query<RealmUserProgress>("userId == $0", userId)
            .first()
            .find() ?: return println("DEBUG: No progress found, skipping achievements")

        println("DEBUG: Progress found - exercises: ${progress.totalExercises}, time: ${progress.totalTime}, streak: ${progress.currentStreak}")

        val achievements = realm.query<RealmAchievement>("userId == $0", userId).find()
        println("DEBUG: Found ${achievements.size} achievements to check")

        var unlockedCount = 0
        realm.write {
            achievements.forEach { realmAchievement ->
                val latestAchievement = findLatest(realmAchievement) ?: return@forEach

                println("DEBUG: Checking achievement: ${latestAchievement.achievementId}, current: ${latestAchievement.currentValue}, target: ${latestAchievement.targetValue}, unlocked: ${latestAchievement.unlockedAt != null}")

                val wasUnlocked = latestAchievement.unlockedAt != null

                when (latestAchievement.achievementId) {
                    "first_exercise" -> {
                        latestAchievement.currentValue = progress.totalExercises
                        if (progress.totalExercises >= 1 && latestAchievement.unlockedAt == null) {
                            println("DEBUG: Unlocking first_exercise!")
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            latestAchievement.progress = 1f
                            unlockedCount++
                        } else {
                            latestAchievement.progress = (progress.totalExercises.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        }
                    }
                    "exercises_5" -> {
                        latestAchievement.currentValue = progress.totalExercises
                        latestAchievement.progress = (progress.totalExercises.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.totalExercises >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "exercises_10" -> {
                        latestAchievement.currentValue = progress.totalExercises
                        latestAchievement.progress = (progress.totalExercises.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.totalExercises >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "exercises_25" -> {
                        latestAchievement.currentValue = progress.totalExercises
                        latestAchievement.progress = (progress.totalExercises.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.totalExercises >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "exercises_50" -> {
                        latestAchievement.currentValue = progress.totalExercises
                        latestAchievement.progress = (progress.totalExercises.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.totalExercises >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "streak_3" -> {
                        latestAchievement.currentValue = progress.currentStreak
                        latestAchievement.progress = (progress.currentStreak.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.currentStreak >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "streak_7" -> {
                        latestAchievement.currentValue = progress.currentStreak
                        latestAchievement.progress = (progress.currentStreak.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.currentStreak >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "streak_14" -> {
                        latestAchievement.currentValue = progress.currentStreak
                        latestAchievement.progress = (progress.currentStreak.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.currentStreak >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "streak_30" -> {
                        latestAchievement.currentValue = progress.currentStreak
                        latestAchievement.progress = (progress.currentStreak.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.currentStreak >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "time_30" -> {
                        latestAchievement.currentValue = progress.totalTime.toInt()
                        latestAchievement.progress = (progress.totalTime.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.totalTime >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "time_60" -> {
                        latestAchievement.currentValue = progress.totalTime.toInt()
                        latestAchievement.progress = (progress.totalTime.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.totalTime >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "time_180" -> {
                        latestAchievement.currentValue = progress.totalTime.toInt()
                        latestAchievement.progress = (progress.totalTime.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.totalTime >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                    "time_300" -> {
                        latestAchievement.currentValue = progress.totalTime.toInt()
                        latestAchievement.progress = (progress.totalTime.toFloat() / latestAchievement.targetValue).coerceAtMost(1f)
                        if (progress.totalTime >= latestAchievement.targetValue && latestAchievement.unlockedAt == null) {
                            latestAchievement.unlockedAt = Clock.System.now().toEpochMilliseconds()
                            unlockedCount++
                        }
                    }
                }

                if (!wasUnlocked && latestAchievement.unlockedAt != null) {
                    println("DEBUG: ✅ ACHIEVEMENT UNLOCKED: ${latestAchievement.achievementId}")
                }
            }
        }
        println("DEBUG: Unlocked $unlockedCount new achievements")
        println("DEBUG: Achievements check completed")
    }

    override suspend fun isAchievementShown(achievementId: String): Boolean {
        val shown = realm.query<RealmShownAchievement>(
            "userId == $0 AND achievementId == $1",
            userId, achievementId
        ).find().firstOrNull()
        return shown != null
    }

    override suspend fun markAchievementAsShown(achievementId: String) {
        realm.write {
            copyToRealm(RealmShownAchievement().apply {
                this.userId = this@RealAchievementRepository.userId
                this.achievementId = achievementId
            })
        }
        println("DEBUG: ✅ Achievement marked as shown: $achievementId")
    }

    private fun convertToDomainAchievement(realmAchievement: RealmAchievement): Achievement {
        return Achievement(
            id = realmAchievement.achievementId,
            title = realmAchievement.title,
            description = realmAchievement.description,
            icon = realmAchievement.icon,
            unlockedAt = realmAchievement.unlockedAt?.let { Instant.fromEpochMilliseconds(it) },
            progress = realmAchievement.progress,
            targetValue = realmAchievement.targetValue,
            currentValue = realmAchievement.currentValue,
            category = realmAchievement.category
        )
    }

    private suspend fun createInitialAchievements(): List<Achievement> {
        println("DEBUG: Creating initial achievements")
        val initialAchievements = listOf(
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "first_exercise"
                title = "Первые шаги"
                description = "Выполните первое упражнение"
                icon = "🎯"
                targetValue = 1
                currentValue = 0
                progress = 0f
                category = "exercises"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "exercises_5"
                title = "Начало пути"
                description = "Выполните 5 упражнений"
                icon = "🚶"
                targetValue = 5
                currentValue = 0
                progress = 0f
                category = "exercises"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "exercises_10"
                title = "Опытный тренирующийся"
                description = "Выполните 10 упражнений"
                icon = "💪"
                targetValue = 10
                currentValue = 0
                progress = 0f
                category = "exercises"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "exercises_25"
                title = "Мастер тренировок"
                description = "Выполните 25 упражнений"
                icon = "🏅"
                targetValue = 25
                currentValue = 0
                progress = 0f
                category = "exercises"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "exercises_50"
                title = "Легенда тренировок"
                description = "Выполните 50 упражнений"
                icon = "👑"
                targetValue = 50
                currentValue = 0
                progress = 0f
                category = "exercises"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "streak_3"
                title = "Регулярность"
                description = "Тренируйтесь 3 дня подряд"
                icon = "📅"
                targetValue = 3
                currentValue = 0
                progress = 0f
                category = "streak"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "streak_7"
                title = "Неделя тренировок"
                description = "Тренируйтесь 7 дней подряд"
                icon = "🔥"
                targetValue = 7
                currentValue = 0
                progress = 0f
                category = "streak"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "streak_14"
                title = "Две недели подряд"
                description = "Тренируйтесь 14 дней подряд"
                icon = "🌟"
                targetValue = 14
                currentValue = 0
                progress = 0f
                category = "streak"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "streak_30"
                title = "Месяц тренировок"
                description = "Тренируйтесь 30 дней подряд"
                icon = "🏆"
                targetValue = 30
                currentValue = 0
                progress = 0f
                category = "streak"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "time_30"
                title = "Полчаса тренировок"
                description = "Потратьте 30 минут на тренировки"
                icon = "⏱️"
                targetValue = 30
                currentValue = 0
                progress = 0f
                category = "time"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "time_60"
                title = "Час тренировок"
                description = "Потратьте 60 минут на тренировки"
                icon = "🕐"
                targetValue = 60
                currentValue = 0
                progress = 0f
                category = "time"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "time_180"
                title = "Три часа тренировок"
                description = "Потратьте 180 минут на тренировки"
                icon = "🕒"
                targetValue = 180
                currentValue = 0
                progress = 0f
                category = "time"
            },
            RealmAchievement().apply {
                userId = this@RealAchievementRepository.userId
                achievementId = "time_300"
                title = "Пять часов тренировок"
                description = "Потратьте 300 минут на тренировки"
                icon = "⭐"
                targetValue = 300
                currentValue = 0
                progress = 0f
                category = "time"
            }
        )

        realm.write {
            initialAchievements.forEach { achievement ->
                copyToRealm(achievement)
            }
        }

        println("DEBUG: Created ${initialAchievements.size} initial achievements")
        return initialAchievements.map { convertToDomainAchievement(it) }
    }
}