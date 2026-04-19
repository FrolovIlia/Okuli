package com.pixelrabbit.backy.ads

expect class AppOpenAdManager() {
    fun initialize(onComplete: () -> Unit)
    fun loadAd()
    fun showIfAvailable(onAdDismissed: () -> Unit)
    fun preloadAd()
    fun isAdAvailable(): Boolean
    val lastAdShowTime: Long
}