// ios/src/iosMain/kotlin/com/pixelrabbit/oculi/utils/PlatformPreferences.kt
package com.pixelrabbit.oculi.utils

import platform.Foundation.NSUserDefaults

actual object PlatformPreferences {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return userDefaults.boolForKey(key) ?: defaultValue
    }

    actual fun setBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
    }
}