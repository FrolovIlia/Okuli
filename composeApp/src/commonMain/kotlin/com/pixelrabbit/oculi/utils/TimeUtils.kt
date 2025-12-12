package com.pixelrabbit.oculi.utils

fun formatTotalTime(totalMinutes: Long): String {
    if (totalMinutes < 60) {
        return "$totalMinutes мин"
    }

    val hours = totalMinutes / 60
    val remainingMinutes = totalMinutes % 60

    return if (remainingMinutes == 0L) {
        "$hours ч"
    } else {
        "$hours ч $remainingMinutes мин"
    }
}