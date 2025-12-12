package com.pixelrabbit.oculi.utils.platform

expect class AndroidContext private constructor() {
    val androidContext: Any

    companion object {
        fun initialize(context: Any)
        fun getInstance(): AndroidContext
    }
}