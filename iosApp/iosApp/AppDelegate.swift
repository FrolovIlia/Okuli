import UIKit
import SwiftUI
import shared

@main
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?
    private let adController = AppOpenAdController.shared
    private let notificationManager = NotificationManager.shared
    private var shouldShowAd = false

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {

        window = UIWindow(frame: UIScreen.main.bounds)

        let loadingView = LoadingScreen(shouldShowAd: $shouldShowAd)
        window?.rootViewController = UIHostingController(rootView: loadingView)
        window?.makeKeyAndVisible()

        notificationManager.requestPermission()

        DispatchQueue.global(qos: .userInitiated).async {
            let adUseCase = ServiceLocator().adUseCase

            Task {
                self.shouldShowAd = await adUseCase.trackAppLaunch()
                let launchCount = await adUseCase.getLaunchCount()

                print("📱 iOS AppLaunch: Launch #\(launchCount), should show ads: \(self.shouldShowAd)")

                DispatchQueue.main.async {
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

    func applicationDidBecomeActive(_ application: UIApplication) {
        notificationManager.updateLastVisitDate()
    }

    private func showAd() {
        adController.loadAd()

        let adLoadingView = AdLoadingView {
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