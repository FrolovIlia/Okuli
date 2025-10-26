package com.pixelrabbit.oculi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform