package com.pixelrabbit.oculi.data.db

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import com.pixelrabbit.oculi.data.models.RealmUserProgress
import com.pixelrabbit.oculi.data.models.RealmAchievement
import com.pixelrabbit.oculi.data.models.RealmExerciseCompletion

object RealmManager {
    private var realm: Realm? = null

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
        }
        return realm!!
    }

    fun close() {
        realm?.close()
        realm = null
    }
}