package com.pixelrabbit.backy.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getGreeting(): String {
    val currentTime = Clock.System.now()
    val localTime = currentTime.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = localTime.hour

    return when {
        hour < 12 -> "Доброе утро"
        hour < 18 -> "Добрый день"
        else -> "Добрый вечер"
    }
}