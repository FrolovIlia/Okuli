// android/src/androidMain/kotlin/com/pixelrabbit/oculi/platform/AndroidContext.kt
package com.pixelrabbit.oculi.utils.platform

import android.content.Context

lateinit var androidContext: Context

fun initializeAndroidContext(context: Context) {
    androidContext = context
}