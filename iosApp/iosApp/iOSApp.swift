// iosApp/iosApp/iOSApp.swift
import SwiftUI
import ComposeApp // Убедись, что название модуля совпадает с твоим (ComposeApp или shared)
import YandexMobileAds

@main
struct iOSApp: App {

    @Environment(\.scenePhase) private var scenePhase
    private let adManager = AppOpenAdManager()

    init() {
        // 1. Инициализация ServiceLocator для уведомлений
        ServiceLocator.shared.init(notificationManager: NotificationManager())

        // 2. Инициализация Яндекс рекламы
        YMAMobileAds.enableLogging(true)
        YMAMobileAds.initialize {
            print("Yandex Mobile Ads initialized")
            adManager.preloadAd()
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea()
        }
        .onChange(of: scenePhase) { newPhase in
            if newPhase == .active {
                // Логика показа рекламы при возврате
                let currentTime = Date().timeIntervalSince1970 * 1000
                // Здесь стоит добавить сохранение времени последнего показа в переменную класса

                if adManager.isAdAvailable() {
                    adManager.showIfAvailable {
                        print("App open ad dismissed")
                    }
                }
            }
        }
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        // Вызываем MainViewController из твоего Kotlin кода
        return Main_iosKt.MainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}