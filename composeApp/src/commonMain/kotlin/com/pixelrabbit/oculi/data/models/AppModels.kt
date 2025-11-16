package com.pixelrabbit.oculi.data.models

import org.mongodb.kbson.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class RealmUserProgress : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userId: String = "default_user"
    var totalExercises: Int = 0
    var totalTime: Long = 0
    var currentStreak: Int = 0
    var todayExercises: Int = 0
    var lastActivityDate: Long = Clock.System.now().toEpochMilliseconds() // Инициализируем
    var createdAt: Long = Clock.System.now().toEpochMilliseconds() // Инициализируем
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds() // Инициализируем
}

class RealmAchievement : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userId: String = "default_user"
    var achievementId: String = ""
    var title: String = ""
    var description: String = ""
    var icon: String = ""
    var unlockedAt: Long? = null // Заменяем Instant на Long
    var progress: Float = 0f
    var targetValue: Int = 0
    var currentValue: Int = 0
    var category: String = ""
}

class RealmExerciseCompletion : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userId: String = "default_user"
    var exerciseId: String = ""
    var exerciseName: String = ""
    var duration: Int = 0
    var completedAt: Long = Clock.System.now().toEpochMilliseconds() // Инициализируем
    var difficulty: String = ""
    var successRate: Float = 0f
}