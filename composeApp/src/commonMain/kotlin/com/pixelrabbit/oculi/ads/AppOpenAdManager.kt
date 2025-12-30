// commonMain/kotlin/com/pixelrabbit/oculi/ads/AppOpenAdManager.kt
package com.pixelrabbit.oculi.ads

expect class AppOpenAdManager() {
    fun initialize(onComplete: () -> Unit)
    fun loadAd()
    fun showIfAvailable(onAdDismissed: () -> Unit)
    fun preloadAd()
    fun isAdAvailable(): Boolean
    val lastAdShowTime: Long
}