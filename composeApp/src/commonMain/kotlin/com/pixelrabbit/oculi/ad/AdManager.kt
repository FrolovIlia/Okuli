package com.pixelrabbit.oculi.ad

expect class AdManager() {
    fun initializeSdk()
    fun loadInterstitialAd(adUnitId: String)
    fun showInterstitialAd()
    fun setupBannerAd(adUnitId: String, container: Any?)
    fun destroyBannerAd()
}