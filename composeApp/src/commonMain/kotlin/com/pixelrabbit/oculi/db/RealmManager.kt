package com.pixelrabbit.oculi.data.db

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import com.pixelrabbit.oculi.data.models.RealmUserProgress
import com.pixelrabbit.oculi.data.models.RealmAchievement
import com.pixelrabbit.oculi.data.models.RealmExerciseCompletion
import io.realm.kotlin.ext.query
import kotlinx.coroutines.runBlocking

object RealmManager {
    private var realm: Realm? = null
    private const val USER_ID = "default_user"
    private var isInitialized = false

    fun getRealm(): Realm {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    RealmUserProgress::class,
                    RealmAchievement::class,
                    RealmExerciseCompletion::class
                )
            )
                .schemaVersion(1)
                .build()

            realm = Realm.open(config)

            // ГАРАНТИРОВАННО создаем прогресс при первом открытии Realm
            runBlocking {
                initializeDefaultData()
            }
        }
        return realm!!
    }

    private suspend fun initializeDefaultData() {
        if (isInitialized) return

        val realm = realm ?: return

        // Ждем пока Realm полностью инициализируется
        realm.write {
            // Проверяем существует ли прогресс
            val existingProgress = query<RealmUserProgress>("userId == $0", USER_ID).first().find()
            if (existingProgress == null) {
                // СОЗДАЕМ ПРОГРЕСС ГАРАНТИРОВАННО
                copyToRealm(RealmUserProgress().apply {
                    userId = USER_ID
                    totalExercises = 0
                    totalTime = 0
                    currentStreak = 0
                    todayExercises = 0
                    lastActivityDate = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                    createdAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                    updatedAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                })
                println("DEBUG: RealmManager - DEFAULT USER PROGRESS CREATED")
            } else {
                println("DEBUG: RealmManager - User progress already exists: ${existingProgress.totalExercises} exercises")
            }
        }

        isInitialized = true
        println("DEBUG: RealmManager - Initialization completed")
    }

    fun close() {
        realm?.close()
        realm = null
        isInitialized = false
    }
}