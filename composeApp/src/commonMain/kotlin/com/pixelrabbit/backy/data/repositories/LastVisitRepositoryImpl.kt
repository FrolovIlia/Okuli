package com.pixelrabbit.backy.data.repositories

import com.pixelrabbit.backy.utils.RealmManager
import com.pixelrabbit.backy.data.models.LastVisit
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class LastVisitRepositoryImpl : LastVisitRepository {

    private val realm: Realm by lazy {
        RealmManager.getRealm()
    }

    // В методе updateLastVisit():
    override suspend fun updateLastVisit() {
        realm.write {
            val existing = query<LastVisit>("userId == 'default'").find().firstOrNull()
            if (existing != null) {
                findLatest(existing)?.timestamp = Clock.System.now().toEpochMilliseconds()
            } else {
                copyToRealm(LastVisit().apply {
                    userId = "default"
                    timestamp = Clock.System.now().toEpochMilliseconds()
                })
            }
        }
    }

    // В методе getLastVisitDate():
    override suspend fun getLastVisitDate(): kotlinx.datetime.LocalDate? {
        val lastVisit = realm.query<LastVisit>("userId == 'default'")
            .first()
            .find()

        return lastVisit?.let {
            // Конвертируем timestamp обратно в LocalDate
            kotlinx.datetime.Instant.fromEpochMilliseconds(it.timestamp)
                .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                .date
        }
    }

    override fun observeLastVisit(): Flow<LocalDate?> {
        return realm.query<LastVisit>("userId == 'default'")
            .asFlow()
            .map { result ->
                result.list.firstOrNull()?.date
            }
    }
}