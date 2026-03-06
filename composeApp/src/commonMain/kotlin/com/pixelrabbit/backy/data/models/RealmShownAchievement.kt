package com.pixelrabbit.backy.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class RealmShownAchievement : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var userId: String = "default_user"
    var achievementId: String = ""
    var shownAt: Long = System.currentTimeMillis()
}