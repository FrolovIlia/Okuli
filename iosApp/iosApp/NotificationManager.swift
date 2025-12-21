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

        center.getNotificationSettings { settings in
            switch settings.authorizationStatus {
            case .authorized:
                self.scheduleDailyReminderIfNeeded()

            case .notDetermined:
                center.requestAuthorization(options: [.alert, .sound, .badge]) { granted, _ in
                    if granted {
                        self.scheduleDailyReminderIfNeeded()
                    }
                }

            default:
                break
            }
        }
    }

    private func scheduleDailyReminderIfNeeded() {
        // Получаем дату последнего визита из KMP
        let lastVisitDate = ServiceLocator().lastVisitRepository().getLastVisit()
        let calendar = Calendar.current

        if let lastVisit = lastVisitDate {
            if calendar.isDateInToday(lastVisit) {
                // Пользователь сегодня заходил — уведомление не ставим
                return
            }
        }

        scheduleDailyReminder()
    }

    private func scheduleDailyReminder() {
        let center = UNUserNotificationCenter.current()
        center.removePendingNotificationRequests(withIdentifiers: ["daily_reminder"])

        let content = UNMutableNotificationContent()
        content.title = "Пора тренировать зрение!"
        content.body = "Не забывайте о ежедневных упражнениях"
        content.sound = .default

        // Уведомление каждый день в 20:00
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
                print("Ошибка добавления уведомления: \(error)")
            }
        }
    }

    // Баннер/звук при переднем плане
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification,
        withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void
    ) {
        completionHandler([.banner, .sound])
    }

    // Обработка нажатия на уведомление — открываем главный экран
    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        didReceive response: UNNotificationResponse,
        withCompletionHandler completionHandler: @escaping () -> Void
    ) {
        DispatchQueue.main.async {
            (UIApplication.shared.delegate as? AppDelegate)?.showMainApp()
        }
        completionHandler()
    }
}
