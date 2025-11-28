package com.pixelrabbit.oculi.ad

import android.content.Context
import android.widget.FrameLayout
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.MobileAds

actual class AdManager actual constructor() {

    private var bannerAdView: BannerAdView? = null
    private var context: Context? = null

    fun setContext(context: Context) {
        this.context = context
    }

    actual fun initializeSdk() {
        context?.let { ctx ->
            MobileAds.initialize(ctx) {
                println("Yandex Mobile Ads SDK initialized")
            }
        }
    }

    actual fun loadInterstitialAd(adUnitId: String) {
        // Временная заглушка для interstitial
        // Нужно будет настроить после проверки баннера
        println("Interstitial ad loading (stub): $adUnitId")
    }

    actual fun showInterstitialAd() {
        // Временная заглушка для interstitial
        println("Interstitial ad shown (stub)")
    }

    actual fun setupBannerAd(adUnitId: String, container: Any?) {
        if (container is FrameLayout) {
            context?.let { ctx ->
                bannerAdView = BannerAdView(ctx)
                bannerAdView?.setAdUnitId(adUnitId)

                container.addView(bannerAdView)

                val adRequest = AdRequest.Builder().build()
                bannerAdView?.loadAd(adRequest)

                println("Banner ad setup with ID: $adUnitId")
            }
        }
    }

    actual fun destroyBannerAd() {
        bannerAdView?.destroy()
        bannerAdView = null
        println("Banner ad destroyed")
    }
}