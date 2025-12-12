import Foundation
import UserNotifications
import shared

class NotificationManager: NSObject, UNUserNotificationCenterDelegate {

    static let shared = NotificationManager()

    private override init() {
        super.init()
        UNUserNotificationCenter.current().delegate = self
    }

    func requestPermission() {
        let center = UNUserNotificationCenter.current()
        center.requestAuthorization(options: [.alert, .sound, .badge]) { granted, error in
            if granted {
                print("Notification permission granted")
                self.scheduleDailyReminder()
            } else if let error = error {
                print("Notification permission error: \(error.localizedDescription)")
            }
        }
    }

    func scheduleDailyReminder() {
        let center = UNUserNotificationCenter.current()
        center.removePendingNotificationRequests(withIdentifiers: ["daily_reminder"])

        let content = UNMutableNotificationContent()
        content.title = "Time for eye training!"
        content.body = "Don't forget your daily exercises"
        content.sound = UNNotificationSound.default

        var dateComponents = DateComponents()
        dateComponents.hour = 20
        dateComponents.minute = 0

        let trigger = UNCalendarNotificationTrigger(
            dateMatching: dateComponents,
            repeats: true
        )

        let request = UNNotificationRequest(
            identifier: "daily_reminder",
            content: content,
            trigger: trigger
        )

        center.add(request) { error in
            if let error = error {
                print("Error scheduling notification: \(error.localizedDescription)")
            } else {
                print("Daily reminder scheduled successfully")
            }
        }
    }

    func updateLastVisitDate() {
        Task {
            do {
                try await ServiceLocator().lastVisitRepository.updateLastVisit()
                print("📱 iOS: Last visit date updated")
            } catch {
                print("📱 iOS: Failed to update last visit date: \(error)")
            }
        }
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .sound])
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        completionHandler()
    }
}