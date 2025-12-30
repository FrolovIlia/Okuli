import UIKit
import SwiftUI
import shared
import Foundation
import YandexMobileAds

@main
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    private let notificationManager = NotificationManager.shared
    private var shouldShowAd = false
    private let adManager = AppOpenAdManager()

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {

        // 1. Инициализация Яндекс рекламы
        YMAMobileAds.enableLogging(true)
        YMAMobileAds.initialize {
            print("✅ Yandex Mobile Ads initialized for iOS")

            // Предзагружаем рекламу после инициализации
            self.adManager.preloadAd()
        }

        // 2. Инициализация AndroidContext для iOS
        self.initializeKMPContext()

        window = UIWindow(frame: UIScreen.main.bounds)

        let loadingView = LoadingScreen(shouldShowAd: $shouldShowAd)
        window?.rootViewController = UIHostingController(rootView: loadingView)
        window?.makeKeyAndVisible()

        // 3. Обновляем дату последнего визита при запуске
        notificationManager.updateLastVisitDate()

        // 4. Запрашиваем разрешение на уведомления
        notificationManager.requestPermission()

        DispatchQueue.global(qos: .userInitiated).async {
            // Используем ServiceLocator как объект
            let serviceLocator = ServiceLocator()

            Task {
                self.shouldShowAd = await serviceLocator.adUseCase.trackAppLaunch()
                let launchCount = await serviceLocator.adUseCase.getLaunchCount()

                print("📱 iOS AppLaunch: Launch #\(launchCount), should show ads: \(self.shouldShowAd)")

                DispatchQueue.main.async {
                    if self.shouldShowAd {
                        print("📱 iOS AppLaunch: Showing ad (after 3+ launches)")
                        self.showAdWithKMPManager()
                    } else {
                        print("📱 iOS AppLaunch: Skipping ad (first 2 launches)")
                        self.showMainApp()
                    }
                }
            }
        }

        return true
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        notificationManager.updateLastVisitDate()
    }

    // MARK: - Вспомогательные методы

    private func initializeKMPContext() {
        // Инициализируем AndroidContext для iOS
        let dummyContext = NSObject()
        AndroidContext().initialize(context: dummyContext)
        print("✅ AndroidContext initialized for iOS")
    }

    private func showAdWithKMPManager() {
        let adLoadingView = AdLoadingView {
            if self.adManager.isAdAvailable() {
                self.adManager.showIfAvailable {
                    DispatchQueue.main.async {
                        self.showMainApp()
                    }
                }
            } else {
                self.showMainApp()
            }
        }

        window?.rootViewController = UIHostingController(rootView: adLoadingView)
    }

    private func showMainApp() {
        let contentView = OculiApp(appOpenAdManager: adManager)
        window?.rootViewController = UIHostingController(rootView: contentView)
    }
}

// Остальные структуры остаются без изменений
struct LoadingScreen: View {
    @Binding var shouldShowAd: Bool
    @State private var isLoading = true

    var body: some View {
        ZStack {
            Color(.systemBackground)
                .ignoresSafeArea()

            VStack {
                ProgressView()
                    .scaleEffect(1.5)
                    .padding()

                Text("Загрузка...")
                    .font(.headline)
                    .padding(.top)
            }
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                isLoading = false
            }
        }
    }
}

struct AdLoadingView: View {
    let onAdLoaded: () -> Void
    @State private var timer = Timer.publish(every: 0.5, on: .main, in: .common).autoconnect()
    @State private var attempts = 0
    private let maxAttempts = 10

    var body: some View {
        ZStack {
            Color(.systemBackground)
                .ignoresSafeArea()

            VStack {
                ProgressView()
                    .scaleEffect(1.5)
                    .padding()

                Text("Подготовка приложения...")
                    .font(.headline)
                    .padding(.top)

                if attempts > 3 {
                    Text("Это может занять несколько секунд")
                        .font(.caption)
                        .foregroundColor(.secondary)
                        .padding(.top, 4)
                }
            }
        }
        .onReceive(timer) { _ in
            attempts += 1
            if attempts >= maxAttempts {
                timer.upstream.connect().cancel()
                onAdLoaded()
            }
        }
        .onAppear {
            DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                timer.upstream.connect().cancel()
                onAdLoaded()
            }
        }
    }
}