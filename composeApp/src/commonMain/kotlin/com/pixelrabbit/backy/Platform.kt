package com.pixelrabbit.backy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform