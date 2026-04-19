package com.pixelrabbit.backy.di

import com.pixelrabbit.backy.data.repositories.SettingsRepositoryImpl
import com.pixelrabbit.backy.domain.repositories.SettingsRepository

actual fun createSettingsRepository(): SettingsRepository {
    return SettingsRepositoryImpl()
}