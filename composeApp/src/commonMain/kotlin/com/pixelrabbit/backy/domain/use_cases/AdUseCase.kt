package com.pixelrabbit.backy.domain.use_cases

import com.pixelrabbit.backy.domain.repositories.SettingsRepository

class AdUseCase(private val settingsRepository: SettingsRepository) {

    suspend fun trackAppLaunch(): Boolean {
        return settingsRepository.incrementLaunchCount()
    }

    suspend fun shouldShowAds(): Boolean {
        return settingsRepository.shouldShowAds()
    }

    suspend fun getLaunchCount(): Int {
        return settingsRepository.getLaunchCount()
    }
}