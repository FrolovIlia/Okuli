// androidMain/kotlin/com/pixelrabbit/oculi/ads/AppOpenAdManager.kt
package com.pixelrabbit.oculi.ads

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.MobileAds
import java.util.concurrent.atomic.AtomicBoolean

actual class AppOpenAdManager actual constructor() {

    private var appOpenAd: AppOpenAd? = null
    private var isLoading = false
    private var isShowingAd = AtomicBoolean(false)
    private val adUnitId = "R-M-17896552-1"
    private val tag = "AppOpenAdManager"
    private var activity: Activity? = null
    private var _lastAdShowTime: Long = 0L

    // ИЗМЕНЕНИЕ: val вместо var
    actual val lastAdShowTime: Long
        get() = _lastAdShowTime

    fun attachActivity(activity: Activity) {
        this.activity = activity
    }

    actual fun initialize(onComplete: () -> Unit) {
        val act = activity ?: return
        MobileAds.initialize(act) {
            Log.d(tag, "Yandex MobileAds SDK успешно инициализирован")
            onComplete()
        }
    }

    actual fun loadAd() {
        val act = activity ?: return

        if (isLoading || appOpenAd != null) {
            Log.d(tag, "Пропускаем загрузку")
            return
        }

        isLoading = true
        Log.d(tag, "Начинаем загрузку рекламы")

        val loader = AppOpenAdLoader(act.application)

        val appOpenAdLoadListener = object : AppOpenAdLoadListener {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                Log.d(tag, "Реклама успешно загружена")
                this@AppOpenAdManager.appOpenAd = appOpenAd
                isLoading = false
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                Log.e(tag, "Ошибка загрузки рекламы: ${error.description}")
                this@AppOpenAdManager.appOpenAd = null
                isLoading = false
            }
        }

        loader.setAdLoadListener(appOpenAdLoadListener)
        val adRequestConfiguration = AdRequestConfiguration.Builder(adUnitId).build()
        loader.loadAd(adRequestConfiguration)
    }

    actual fun showIfAvailable(onAdDismissed: () -> Unit) {
        val act = activity ?: return

        Log.d(tag, "Попытка показа рекламы. Доступна: ${appOpenAd != null}")

        if (isShowingAd.get()) {
            Log.d(tag, "Реклама уже показывается, пропускаем")
            return
        }

        if (act.isFinishing || act.isDestroyed) {
            Log.w(tag, "Activity завершается или уничтожена")
            onAdDismissed()
            return
        }

        val ad = appOpenAd
        if (ad == null) {
            Log.d(tag, "Нет доступной рекламы для показа")
            onAdDismissed()
            return
        }

        if (!isShowingAd.compareAndSet(false, true)) {
            Log.d(tag, "Не удалось установить флаг показа")
            return
        }

        appOpenAd = null

        val appOpenAdEventListener = object : AppOpenAdEventListener {
            override fun onAdShown() {
                Log.d(tag, "Реклама показана")
            }

            override fun onAdFailedToShow(adError: AdError) {
                Log.e(tag, "Ошибка показа рекламы: ${adError.description}")
                cleanup()
                onAdDismissed()
            }

            override fun onAdDismissed() {
                Log.d(tag, "Реклама закрыта пользователем")
                cleanup()
                onAdDismissed()
                Handler(Looper.getMainLooper()).postDelayed({
                    loadAd()
                }, 2000L)
            }

            override fun onAdClicked() {
                Log.d(tag, "Реклама была кликнута")
            }

            override fun onAdImpression(impressionData: ImpressionData?) {
                Log.d(tag, "Зафиксирован показ (impression)")
            }
        }

        ad.setAdEventListener(appOpenAdEventListener)

        try {
            _lastAdShowTime = System.currentTimeMillis() // Используем приватное поле
            ad.show(act)
        } catch (e: Exception) {
            Log.e(tag, "Исключение при показе рекламы: ${e.message}")
            cleanup()
            onAdDismissed()
        }
    }

    private fun cleanup() {
        isShowingAd.set(false)
        appOpenAd?.setAdEventListener(null)
        appOpenAd = null
    }

    actual fun preloadAd() {
        if (appOpenAd == null && !isLoading) {
            Log.d(tag, "Предзагрузка рекламы")
            loadAd()
        }
    }

    actual fun isAdAvailable(): Boolean {
        return appOpenAd != null && !isShowingAd.get()
    }
}