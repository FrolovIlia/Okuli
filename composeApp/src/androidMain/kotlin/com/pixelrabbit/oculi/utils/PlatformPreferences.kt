// android/src/androidMain/kotlin/com/pixelrabbit/oculi/utils/PlatformPreferences.kt
package com.pixelrabbit.oculi.utils

import android.content.Context
import com.pixelrabbit.oculi.utils.platform.androidContext

actual object PlatformPreferences {
    private val sharedPreferences by lazy {
        androidContext.getSharedPreferences("oculi_prefs", Context.MODE_PRIVATE)
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    actual fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    actual fun setInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }
}