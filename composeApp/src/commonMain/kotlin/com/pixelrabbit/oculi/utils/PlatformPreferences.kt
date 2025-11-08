// shared/src/commonMain/kotlin/com/pixelrabbit/oculi/utils/PlatformPreferences.kt
package com.pixelrabbit.oculi.utils

expect object PlatformPreferences {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun setBoolean(key: String, value: Boolean)
}