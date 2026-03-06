package com.pixelrabbit.backy.data.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LastVisit : RealmObject {
    @PrimaryKey
    var userId: String = ""

    // Вместо LocalDate храним timestamp в миллисекундах
    var timestamp: Long = 0

    // Вычисляемое свойство для удобства (не хранится в базе)
    val date: kotlinx.datetime.LocalDate
        get() = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
}