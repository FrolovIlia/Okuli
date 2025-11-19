package com.pixelrabbit.oculi.utils

fun formatTotalTime(totalMinutes: Long): String {
    if (totalMinutes < 60) {
        return "$totalMinutes мин"
    }

    val hours = totalMinutes / 60
    val remainingMinutes = totalMinutes % 60

    return if (remainingMinutes == 0L) {
        // Если ровно часы, например, 3 ч
        "$hours ч"
    } else {
        // Если часы и минуты, например, 3 ч 15 мин
        "$hours ч $remainingMinutes мин"
    }
}