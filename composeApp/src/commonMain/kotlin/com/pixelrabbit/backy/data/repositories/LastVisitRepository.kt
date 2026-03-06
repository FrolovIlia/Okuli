package com.pixelrabbit.backy.data.repositories

import kotlinx.datetime.LocalDate
import kotlinx.coroutines.flow.Flow

interface LastVisitRepository {
    suspend fun updateLastVisit()
    suspend fun getLastVisitDate(): LocalDate?
    fun observeLastVisit(): Flow<LocalDate?>
}