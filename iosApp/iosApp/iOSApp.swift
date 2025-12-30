// Если оставляешь iOSApp.swift, обнови его:
import SwiftUI
import ComposeApp
import YandexMobileAds

@main
struct iOSApp: App {

    @Environment(\.scenePhase) private var scenePhase
    private let adManager = AppOpenAdManager()

    init() {
        // Инициализация Яндекс рекламы
        YMAMobileAds.enableLogging(true)
        YMAMobileAds.initialize { 
            print("Yandex Mobile Ads initialized")
            // Предзагружаем рекламу
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
                // Проверяем интервал (2 минуты между показами)
                let currentTime = Date().timeIntervalSince1970 * 1000
                let twoMinutesAgo = currentTime - (2 * 60 * 1000)
                
                if adManager.isAdAvailable() && currentTime > twoMinutesAgo {
                    // Получаем rootViewController для показа
                    if let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
                       let rootVC = windowScene.windows.first?.rootViewController {
                        
                        // Используем KMP менеджер
                        adManager.showIfAvailable {
                            print("App open ad dismissed")
                        }
                    }
                }
            }
        }
    }
}

struct ContentView: View {
    private let adManager = AppOpenAdManager()
    
    var body: some View {
        ComposeView()
            .onAppear {
                // Инициализация при первом показе
                adManager.initialize {
                    adManager.preloadAd()
                }
            }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let adManager = AppOpenAdManager()
        
        // Создаем Compose контроллер с передачей менеджера рекламы
        let composeVC = Main_iosKt.MainViewController()
        return composeVC
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}