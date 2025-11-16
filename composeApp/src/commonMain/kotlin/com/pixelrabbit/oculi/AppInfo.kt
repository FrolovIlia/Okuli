package com.pixelrabbit.oculi

object AppInfo {
    val versionName: String = AppConfig.VERSION_NAME
    val versionCode: Int = AppConfig.VERSION_CODE
    val appName: String = AppConfig.APP_NAME

    // Дополнительная информация о приложении
    val displayVersion: String get() = "$appName v$versionName"
}