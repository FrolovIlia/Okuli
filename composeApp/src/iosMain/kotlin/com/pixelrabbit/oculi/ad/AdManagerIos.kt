package com.pixelrabbit.oculi.ad

import platform.UIKit.UIView

actual class AdManager actual constructor() {

    actual fun initializeSdk() {
        // На iOS инициализация происходит автоматически
    }

    actual fun loadInterstitialAd(adUnitId: String) {
        // Заглушка для iOS
        println("Loading interstitial ad for iOS: $adUnitId")
    }

    actual fun showInterstitialAd() {
        // Заглушка для iOS
        println("Showing interstitial ad for iOS")
    }

    actual fun setupBannerAd(adUnitId: String, container: Any?) {
        // Заглушка для iOS
        println("Setting up banner ad for iOS: $adUnitId")
    }

    actual fun destroyBannerAd() {
        // Заглушка для iOS
        println("Destroying banner ad for iOS")
    }
}