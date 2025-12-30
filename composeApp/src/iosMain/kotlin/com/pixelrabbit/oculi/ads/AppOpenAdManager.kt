// iosMain/kotlin/com/pixelrabbit/oculi/ads/AppOpenAdManager.kt
package com.pixelrabbit.oculi.ads

import cocoapods.YandexMobileAds.YMAAdError
import cocoapods.YandexMobileAds.YMAAdRequest
import cocoapods.YandexMobileAds.YMAAdRequestConfiguration
import cocoapods.YandexMobileAds.YMAAppOpenAd
import cocoapods.YandexMobileAds.YMAAppOpenAdLoader
import cocoapods.YandexMobileAds.YMAMobileAds
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.native.concurrent.AtomicInt
import kotlin.native.concurrent.freeze

@OptIn(ExperimentalForeignApi::class)
actual class AppOpenAdManager actual constructor() {

    private var appOpenAd: YMAAppOpenAd? = null
    private val isLoading = AtomicInt(0)
    private val isShowingAd = AtomicInt(0)
    private val adUnitId = "R-M-17896552-1" // Тот же ID что и на Android

    actual var lastAdShowTime: Long = 0L
        private set

    private var onAdDismissedCallback: (() -> Unit)? = null

    actual fun initialize(onComplete: () -> Unit) {
        YMAMobileAds.enableLogging(true)
        YMAMobileAds.initializeWithCompletionHandler {
            platform.ios UIKit.UIDevice.currentDevice.systemVersion
                    onComplete()
        }
    }

    actual fun loadAd() {
        if (isLoading.value != 0 || appOpenAd != null) return
        isLoading.value = 1

        val loader = YMAAppOpenAdLoader()

        loader.setDelegate(object : NSObject(), YMAAppOpenAdLoaderDelegateProtocol {
            override fun appOpenAdLoader(adLoader: YMAAppOpenAdLoader, didLoad ad: YMAAppOpenAd) {
                this@AppOpenAdManager.appOpenAd = ad
                isLoading.value = 0
            }

            override fun appOpenAdLoader(adLoader: YMAAppOpenAdLoader, didFailToLoadWithError error: YMAAdError) {
                this@AppOpenAdManager.appOpenAd = null
                isLoading.value = 0
            }
        }.freeze())

        val request = YMAAdRequestConfiguration.adRequestConfigurationWithAdUnitID(adUnitId)
        loader.loadAdWithRequestConfiguration(request)
    }

    actual fun showIfAvailable(onAdDismissed: () -> Unit) {
        if (isShowingAd.value != 0) return

        val ad = appOpenAd ?: run {
            onAdDismissed()
            return
        }

        if (!isShowingAd.compareAndSet(0, 1)) {
            onAdDismissed()
            return
        }

        this.onAdDismissedCallback = onAdDismissed
        appOpenAd = null

        ad.setDelegate(object : NSObject(), YMAAppOpenAdDelegateProtocol {
            override fun appOpenAdDidAppear(ad: YMAAppOpenAd) {
                // Реклама показана
            }

            override fun appOpenAdDidFailToShowWithError(ad: YMAAppOpenAd, error: YMAAdError) {
                cleanup()
                onAdDismissedCallback?.invoke()
            }

            override fun appOpenAdDidDisappear(ad: YMAAppOpenAd) {
                cleanup()
                onAdDismissedCallback?.invoke()
                // Загружаем следующую рекламу через 2 секунды
                dispatch_after(
                    dispatch_time(DISPATCH_TIME_NOW, 2_000_000_000),
                    dispatch_get_main_queue()
                ) {
                    loadAd()
                }
            }

            override fun appOpenAdDidClick(ad: YMAAppOpenAd) {
                // Реклама кликнута
            }

            override fun appOpenAd(ad: YMAAppOpenAd, didTrackImpressionWith impressionData: YMAImpressionData?) {
                // Импрессия зафиксирована
            }
        }.freeze())

        lastAdShowTime = platform.Foundation.NSDate.date().timeIntervalSince1970.toLong() * 1000

        // Получаем rootViewController для показа
        val window = platform.ios.UIKit.UIApplication.sharedApplication.windows.firstOrNull()
        val rootViewController = window?.rootViewController
        if (rootViewController != null) {
            ad.presentFromViewController(rootViewController)
        } else {
            cleanup()
            onAdDismissed()
        }
    }

    private fun cleanup() {
        isShowingAd.value = 0
        appOpenAd?.setDelegate(null)
        appOpenAd = null
        onAdDismissedCallback = null
    }

    actual fun preloadAd() {
        if (appOpenAd == null && isLoading.value == 0) {
            loadAd()
        }
    }

    actual fun isAdAvailable(): Boolean {
        return appOpenAd != null && isShowingAd.value == 0
    }
}