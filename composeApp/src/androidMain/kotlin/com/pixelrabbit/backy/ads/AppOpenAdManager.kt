package com.pixelrabbit.backy.ads

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.pixelrabbit.backy.di.ServiceLocator
import com.yandex.mobile.ads.appopenad.AppOpenAd
import com.yandex.mobile.ads.appopenad.AppOpenAdEventListener
import com.yandex.mobile.ads.appopenad.AppOpenAdLoader
import com.yandex.mobile.ads.appopenad.AppOpenAdLoadListener
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.common.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

actual class AppOpenAdManager actual constructor() {

    private var appOpenAd: AppOpenAd? = null
    private var isLoading = false
    private var isShowingAd = AtomicBoolean(false)
    private val adUnitId = "R-M-18885638-1"
    private val tag = "AppOpenAdManager"
    private var activity: Activity? = null
    private var _lastAdShowTime: Long = 0L
    private var retryCount = 0
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    private var periodicReloadHandler: Handler? = null
    private var periodicReloadRunnable: Runnable? = null

    actual val lastAdShowTime: Long
        get() = _lastAdShowTime

    fun attachActivity(activity: Activity) {
        this.activity = activity
        registerNetworkCallback(activity.applicationContext)
        startPeriodicReload()
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

        if (isLoading) {
            Log.d(tag, "Загрузка уже идёт, пропускаем")
            return
        }

        isLoading = true
        Log.d(tag, "Начинаем загрузку рекламы, попытка ${retryCount + 1}")

        val loader = AppOpenAdLoader(act.application)

        val appOpenAdLoadListener = object : AppOpenAdLoadListener {
            override fun onAdLoaded(appOpenAd: AppOpenAd) {
                Log.d(tag, "Реклама успешно загружена")
                this@AppOpenAdManager.appOpenAd = appOpenAd
                isLoading = false
                retryCount = 0
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                Log.e(tag, "Ошибка загрузки рекламы: ${error.description}, код: ${error.code}")
                this@AppOpenAdManager.appOpenAd = null
                isLoading = false

                val delayMs = (retryCount * 5000L).coerceAtMost(60000L)
                retryCount++

                Log.d(tag, "Повторная попытка загрузки через ${delayMs}мс (попытка ${retryCount})")
                Handler(Looper.getMainLooper()).postDelayed({
                    loadAd()
                }, delayMs)
            }
        }

        loader.setAdLoadListener(appOpenAdLoadListener)
        val adRequestConfiguration = AdRequestConfiguration.Builder(adUnitId).build()
        loader.loadAd(adRequestConfiguration)
    }

    fun forceReloadAd() {
        Log.d(tag, "Принудительная перезагрузка рекламы")
        appOpenAd = null
        isLoading = false
        retryCount = 0
        loadAd()
    }

    actual fun showIfAvailable(onAdDismissed: () -> Unit) {
        val act = activity ?: run {
            Log.e(tag, "Activity is null")
            onAdDismissed()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            val adsRemoved = ServiceLocator.settingsRepository().isAdsRemoved()

            if (adsRemoved) {
                Log.d(tag, "Реклама отключена покупкой, не показываем")
                onAdDismissed()
                return@launch
            }

            if (isShowingAd.get()) {
                Log.d(tag, "Реклама уже показывается, пропускаем")
                onAdDismissed()
                return@launch
            }

            if (act.isFinishing || act.isDestroyed) {
                Log.w(tag, "Activity завершается или уничтожена")
                onAdDismissed()
                return@launch
            }

            if (appOpenAd == null) {
                Log.d(tag, "Реклама не готова, ждём до 3 секунд")
                var waitTime = 0L
                val maxWaitTime = 3000L
                val checkInterval = 100L

                while (appOpenAd == null && waitTime < maxWaitTime) {
                    delay(checkInterval)
                    waitTime += checkInterval
                }

                if (appOpenAd == null) {
                    Log.d(tag, "Реклама не загрузилась за $maxWaitTime мс")
                    if (!isLoading) {
                        loadAd()
                    }
                    onAdDismissed()
                    return@launch
                }
            }

            val ad = appOpenAd
            if (ad == null) {
                Log.d(tag, "Нет доступной рекламы для показа")
                if (!isLoading) {
                    loadAd()
                }
                onAdDismissed()
                return@launch
            }

            if (!isShowingAd.compareAndSet(false, true)) {
                Log.d(tag, "Не удалось установить флаг показа")
                return@launch
            }

            appOpenAd = null

            val appOpenAdEventListener = object : AppOpenAdEventListener {
                override fun onAdShown() {
                    Log.d(tag, "Реклама показана")
                    _lastAdShowTime = System.currentTimeMillis()
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
                ad.show(act)
            } catch (e: Exception) {
                Log.e(tag, "Исключение при показе рекламы: ${e.message}")
                cleanup()
                onAdDismissed()
            }
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

    private fun startPeriodicReload() {
        stopPeriodicReload()
        periodicReloadHandler = Handler(Looper.getMainLooper())
        periodicReloadRunnable = object : Runnable {
            override fun run() {
                if (appOpenAd == null && !isLoading && activity != null) {
                    Log.d(tag, "Периодическая проверка: реклама отсутствует, загружаем")
                    loadAd()
                }
                periodicReloadHandler?.postDelayed(this, 30000L)
            }
        }
        periodicReloadHandler?.postDelayed(periodicReloadRunnable!!, 30000L)
        Log.d(tag, "Периодическая перезагрузка запущена (интервал 30 сек)")
    }

    private fun stopPeriodicReload() {
        periodicReloadRunnable?.let { periodicReloadHandler?.removeCallbacks(it) }
        periodicReloadHandler = null
        periodicReloadRunnable = null
        Log.d(tag, "Периодическая перезагрузка остановлена")
    }

    private fun registerNetworkCallback(context: Context) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            private var lastNetworkState = false

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.d(tag, "Сеть доступна")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.d(tag, "Сеть потеряна")
                lastNetworkState = false
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                Log.d(tag, "Интернет доступен: $hasInternet")

                if (hasInternet != lastNetworkState) {
                    lastNetworkState = hasInternet
                    if (hasInternet) {
                        Log.d(tag, "Интернет восстановлен, принудительная перезагрузка")
                        Handler(Looper.getMainLooper()).postDelayed({
                            forceReloadAd()
                        }, 1000)
                    }
                }
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            connectivityManager.registerNetworkCallback(request, callback)
            networkCallback = callback
            Log.d(tag, "NetworkCallback зарегистрирован")
        } catch (e: Exception) {
            Log.e(tag, "Ошибка регистрации NetworkCallback: ${e.message}")
        }
    }

    fun unregisterNetworkCallback() {
        stopPeriodicReload()
        networkCallback?.let {
            try {
                val connectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                connectivityManager?.unregisterNetworkCallback(it)
                Log.d(tag, "NetworkCallback отключен")
            } catch (e: Exception) {
                Log.e(tag, "Ошибка отключения NetworkCallback: ${e.message}")
            }
            networkCallback = null
        }
    }
}