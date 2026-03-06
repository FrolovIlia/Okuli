package com.pixelrabbit.backy.utils

import com.pixelrabbit.backy.data.models.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmManager {
    private var realmInstance: Realm? = null

    fun getRealm(): Realm {
        if (realmInstance == null || realmInstance!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    RealmUserProgress::class,
                    RealmAchievement::class,
                    RealmExerciseCompletion::class,
                    LastVisit::class
                )
            )
                .schemaVersion(1) // Ключевое исправление: увеличиваем версию схемы
                .compactOnLaunch() // Дополнительная оптимизация
                .build()

            realmInstance = Realm.open(config)
        }
        return realmInstance!!
    }

    fun closeRealm() {
        realmInstance?.close()
        realmInstance = null
    }
}