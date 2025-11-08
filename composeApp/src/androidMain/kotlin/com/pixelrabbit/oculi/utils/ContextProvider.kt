// android/src/androidMain/kotlin/com/pixelrabbit/oculi/utils/ContextProvider.kt
package com.pixelrabbit.oculi.utils

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