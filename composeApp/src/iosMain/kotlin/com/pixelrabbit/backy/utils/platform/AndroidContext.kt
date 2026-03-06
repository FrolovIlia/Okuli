package com.pixelrabbit.backy.utils.platform

import platform.Foundation.NSObject

actual class AndroidContext actual constructor() {
    actual val androidContext: Any
        get() = NSObject()

    actual companion object {
        private var instance: AndroidContext? = null

        actual fun initialize(context: Any) {
            if (instance == null) {
                instance = AndroidContext()
            }
        }

        actual fun getInstance(): AndroidContext {
            return instance ?: AndroidContext()
        }
    }
}