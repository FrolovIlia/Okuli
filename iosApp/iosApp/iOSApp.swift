import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    @Environment(\.scenePhase) private var scenePhase

    init() {
        // Загружаем рекламу сразу при старте приложения
        AppOpenAdController.shared.loadAd()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea()
        }
        .onChange(of: scenePhase) { newPhase in
            if newPhase == .active {
                // Показываем App Open Ad при cold start или возврате из background
                if let rootVC = UIApplication.shared.windows.first?.rootViewController {
                    AppOpenAdController.shared.showIfAvailable(from: rootVC) {
                        // UI Compose уже готов
                    }
                }
            }
        }
    }
}
