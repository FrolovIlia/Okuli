package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.data.repositories.LastVisitRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class CheckDailyVisitUseCase(
    private val repository: LastVisitRepository
) {
    suspend fun execute(): Boolean {
        val lastVisit = repository.getLastVisitDate()
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        return lastVisit != today
    }
}