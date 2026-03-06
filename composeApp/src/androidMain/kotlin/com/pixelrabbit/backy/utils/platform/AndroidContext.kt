package com.pixelrabbit.backy.utils.platform

import android.content.Context

actual class AndroidContext private actual constructor() {
    private lateinit var _appContext: Context

    actual val androidContext: Any
        get() = _appContext

    actual companion object {
        private var instance: AndroidContext? = null

        actual fun initialize(context: Any) {
            if (instance == null) {
                instance = AndroidContext()
                instance!!._appContext = context as Context
            }
        }

        actual fun getInstance(): AndroidContext {
            return instance ?: throw IllegalStateException("AndroidContext not initialized")
        }
    }
}