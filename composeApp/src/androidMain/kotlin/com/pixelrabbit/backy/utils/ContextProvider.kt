// android/src/androidMain/kotlin/com/pixelrabbit/backy/utils/ContextProvider.kt
package com.pixelrabbit.backy.utils

import android.app.Application

object ContextProvider {
    private var application: Application? = null

    fun initialize(application: Application) {
        this.application = application
    }

    fun getApplicationContext(): Application {
        return application ?: throw IllegalStateException("ContextProvider not initialized")
    }
}