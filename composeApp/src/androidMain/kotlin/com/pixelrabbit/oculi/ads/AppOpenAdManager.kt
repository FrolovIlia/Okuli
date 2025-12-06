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

    fun initialize(onComplete: (() -> Unit)? = null) {
        MobileAds.initialize(activity) {}

        val loader = AppOpenAdLoader(activity)
        loader.setAdLoadListener(object : AppOpenAdLoadListener {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                this@AppOpenAdManager.appOpenAd = appOpenAd
                showIfAvailable(onComplete)
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                this@AppOpenAdManager.appOpenAd = null
                activity.runOnUiThread { onComplete?.invoke() }
            }
        })

        val request = AdRequestConfiguration.Builder(adUnitId).build()
        loader.loadAd(request)
    }

    fun showIfAvailable(onComplete: (() -> Unit)? = null) {
        val ad = appOpenAd
        if (ad == null || activity.isFinishing || activity.isDestroyed) {
            activity.runOnUiThread { onComplete?.invoke() }
            return
        }

        ad.setAdEventListener(object : AppOpenAdEventListener {
            override fun onAdShown() {}
            override fun onAdDismissed() {
                appOpenAd = null
                activity.runOnUiThread { onComplete?.invoke() }
            }

            override fun onAdFailedToShow(error: AdError) {
                appOpenAd = null
                activity.runOnUiThread { onComplete?.invoke() }
            }

            override fun onAdClicked() {}
            override fun onAdImpression(impressionData: ImpressionData?) {}
        })

        ad.show(activity)
    }
}
