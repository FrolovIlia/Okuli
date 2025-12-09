import UIKit
import SwiftUI
import shared // ваш KMP модуль

@main
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    private let adController = AppOpenAdController.shared
    private var shouldShowAd = false

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {

        // Создаем окно сразу
        window = UIWindow(frame: UIScreen.main.bounds)

        // Показываем экран загрузки
        let loadingView = LoadingScreen(shouldShowAd: $shouldShowAd)
        window?.rootViewController = UIHostingController(rootView: loadingView)
        window?.makeKeyAndVisible()

        // В фоне проверяем нужно ли показывать рекламу
        DispatchQueue.global(qos: .userInitiated).async {
            let adUseCase = ServiceLocator().adUseCase

            Task {
                // 1. Сначала отслеживаем запуск
                self.shouldShowAd = await adUseCase.trackAppLaunch()
                let launchCount = await adUseCase.getLaunchCount()

                print("📱 iOS AppLaunch: Launch #\(launchCount), should show ads: \(self.shouldShowAd)")

                DispatchQueue.main.async {
                    // 2. Решаем показывать рекламу или нет
                    if self.shouldShowAd {
                        print("📱 iOS AppLaunch: Showing ad (after 3+ launches)")
                        self.showAd()
                    } else {
                        print("📱 iOS AppLaunch: Skipping ad (first 2 launches)")
                        self.showMainApp()
                    }
                }
            }
        }

        return true
    }

    private func showAd() {
        // Загружаем рекламу и показываем
        adController.loadAd()

        let adLoadingView = AdLoadingView {
            // Когда реклама загружена, показываем её
            if let rootVC = self.window?.rootViewController {
                self.adController.showIfAvailable(from: rootVC) {
                    self.showMainApp()
                }
            } else {
                self.showMainApp()
            }
        }

        window?.rootViewController = UIHostingController(rootView: adLoadingView)
    }

    private func showMainApp() {
        let contentView = OculiApp()
        window?.rootViewController = UIHostingController(rootView: contentView)
    }
}

// SwiftUI View для экрана загрузки
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
            // Показываем загрузку 1 секунду для плавности
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.0) {
                isLoading = false
            }
        }
    }
}

// SwiftUI View для загрузки рекламы
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
            // Даем рекламе время на загрузку
            DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) {
                timer.upstream.connect().cancel()
                onAdLoaded()
            }
        }
    }
}