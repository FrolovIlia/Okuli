package com.pixelrabbit.backy.db

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import com.pixelrabbit.backy.data.models.*
import io.realm.kotlin.ext.query
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock

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
                    RealmExerciseCompletion::class,
                    RealmShownAchievement::class
                )
            )
                .schemaVersion(2)
                .build()

            realm = Realm.open(config)

            runBlocking {
                initializeDefaultData()
            }
        }
        return realm!!
    }

    private suspend fun initializeDefaultData() {
        if (isInitialized) return

        val realm = realm ?: return

        realm.write {
            val existingProgress = query<RealmUserProgress>("userId == $0", USER_ID).first().find()
            if (existingProgress == null) {
                copyToRealm(RealmUserProgress().apply {
                    userId = USER_ID
                    totalExercises = 0
                    totalTime = 0
                    currentStreak = 0
                    todayExercises = 0
                    lastActivityDate = Clock.System.now().toEpochMilliseconds()
                    createdAt = Clock.System.now().toEpochMilliseconds()
                    updatedAt = Clock.System.now().toEpochMilliseconds()
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