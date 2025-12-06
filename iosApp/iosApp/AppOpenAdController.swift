import UIKit
import YandexMobileAds

class AppOpenAdController: NSObject {
    static let shared = AppOpenAdController()
    private var appOpenAd: AppOpenAd?
    private let adUnitId = "R-M-17933958-1" // iOS блок
    private var onComplete: (() -> Void)?

    private override init() {
        super.init()
        YMMYandexMobileAds.initialize() // SDK инициализация
    }

    func loadAd() {
        let loader = AppOpenAdLoader()
        loader.delegate = self
        let request = AdRequestConfiguration(adUnitID: adUnitId)
        loader.loadAd(with: request)
    }

    func showIfAvailable(from root: UIViewController, onComplete: @escaping () -> Void) {
        guard let ad = appOpenAd else {
            onComplete()
            return
        }
        self.onComplete = onComplete
        ad.delegate = self
        ad.show(from: root)
    }
}

extension AppOpenAdController: AppOpenAdLoaderDelegate {
    func appOpenAdLoader(_ adLoader: AppOpenAdLoader, didLoad appOpenAd: AppOpenAd) {
        self.appOpenAd = appOpenAd
    }

    func appOpenAdLoader(_ adLoader: AppOpenAdLoader, didFailToLoadWithError error: AdRequestError) {
        self.appOpenAd = nil
    }
}

extension AppOpenAdController: AppOpenAdDelegate {
    func appOpenAdDidShow(_ ad: AppOpenAd) {}
    func appOpenAd(_ ad: AppOpenAd, didFailToShowWithError error: Error) {
        appOpenAd = nil
        onComplete?()
    }
    func appOpenAdDidDismiss(_ ad: AppOpenAd) {
        appOpenAd = nil
        onComplete?()
    }
    func appOpenAdDidClick(_ ad: AppOpenAd) {}
    func appOpenAd(_ ad: AppOpenAd, didTrackImpressionWith impressionData: ImpressionData?) {}
}
