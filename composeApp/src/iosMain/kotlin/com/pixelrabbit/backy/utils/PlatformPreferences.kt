// ios/src/iosMain/kotlin/com/pixelrabbit/backy/utils/PlatformPreferences.kt
package com.pixelrabbit.backy.utils

import platform.Foundation.NSUserDefaults

actual object PlatformPreferences {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        // NSUserDefaults возвращает NSNumber? для boolForKey
        val value = userDefaults.objectForKey(key) as? NSNumber
        return value?.boolValue ?: defaultValue
    }

    actual fun setBoolean(key: String, value: Boolean) {
        userDefaults.setBool(value, key)
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return userDefaults.integerForKey(key).toInt()
    }

    actual fun setInt(key: String, value: Int) {
        userDefaults.setInteger(value.toLong(), key)
    }
}