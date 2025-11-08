// shared/src/commonMain/kotlin/com/pixelrabbit/oculi/utils/AppSettings.kt
package com.pixelrabbit.oculi.utils

class AppSettings {
    var isOnboardingCompleted: Boolean
        get() = PlatformPreferences.getBoolean(ONBOARDING_COMPLETED_KEY, false)
        set(value) = PlatformPreferences.setBoolean(ONBOARDING_COMPLETED_KEY, value)

    companion object {
        private const val ONBOARDING_COMPLETED_KEY = "onboarding_completed"
    }
}