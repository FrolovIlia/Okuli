package com.pixelrabbit.backy.utils

import com.pixelrabbit.backy.utils.platform.AndroidContext

actual object PlatformPreferences {
    private val sharedPreferences by lazy {
        val context = AndroidContext.getInstance().androidContext as android.content.Context
        context.getSharedPreferences("backy_prefs", android.content.Context.MODE_PRIVATE)
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    actual fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    actual fun getInt(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    actual fun setInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    actual fun getString(key: String, defaultValue: String?): String? =
        sharedPreferences.getString(key, defaultValue)

    actual fun setString(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}