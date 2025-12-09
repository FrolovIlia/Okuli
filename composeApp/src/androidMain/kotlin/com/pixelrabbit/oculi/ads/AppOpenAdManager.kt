package com.pixelrabbit.oculi.ads

import android.app.Activity
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.MobileAds

class AppOpenAdManager(private val activity: Activity) {

    private var appOpenAd: AppOpenAd? = null
    private val adUnitId = "R-M-17896552-1" // Android блок
    private var onAdDismissed: (() -> Unit)? = null

    fun initialize(onComplete: () -> Unit) {
        MobileAds.initialize(activity) {
            val loader = AppOpenAdLoader(activity)
            loader.setAdLoadListener(object : AppOpenAdLoadListener {
                override fun onAdLoaded(appOpenAd: AppOpenAd) {
                    this@AppOpenAdManager.appOpenAd = appOpenAd
                    onComplete()
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    this@AppOpenAdManager.appOpenAd = null
                    onComplete()
                }
            })

            val request = AdRequestConfiguration.Builder(adUnitId).build()
            loader.loadAd(request)
        }
    }

    fun showIfAvailable(onAdDismissed: () -> Unit) {
        this.onAdDismissed = onAdDismissed
        val ad = appOpenAd

        if (ad == null || activity.isFinishing || activity.isDestroyed) {
            onAdDismissed()
            return
        }

        ad.setAdEventListener(object : AppOpenAdEventListener {
            override fun onAdShown() {}

            override fun onAdDismissed() {
                appOpenAd = null
                onAdDismissed()
            }

            override fun onAdFailedToShow(error: AdError) {
                appOpenAd = null
                onAdDismissed()
            }

            override fun onAdClicked() {}
            override fun onAdImpression(impressionData: ImpressionData?) {}
        })

        ad.show(activity)
    }
}