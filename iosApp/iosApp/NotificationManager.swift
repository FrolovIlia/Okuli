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

    // MARK: - Обновление даты последнего визита
    func updateLastVisitDate() {
        DispatchQueue.global(qos: .userInitiated).async {
            // Используем ServiceLocator как object (синглтон)
            let serviceLocator = ServiceLocator()
            Task {
                await serviceLocator.lastVisitRepository().updateLastVisit()
            }
        }
    }

    // MARK: - Планирование напоминаний
    private func scheduleDailyReminderIfNeeded() {
        DispatchQueue.global(qos: .userInitiated).async {
            let serviceLocator = ServiceLocator()

            Task {
                // Получаем дату последнего визита из KMP (правильный метод)
                guard let lastVisitLocalDate = await serviceLocator.lastVisitRepository().getLastVisitDate() else {
                    // Если даты нет (первый запуск), ставим напоминание
                    self.scheduleDailyReminder()
                    return
                }

                // Проверяем, был ли пользователь сегодня
                let wasUserToday = self.isLocalDateToday(lastVisitLocalDate)

                if !wasUserToday {
                    self.scheduleDailyReminder()
                } else {
                    print("Пользователь сегодня уже заходил, напоминание не ставится")
                }
            }
        }
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
            } else {
                print("Ежедневное напоминание запланировано на 20:00")
            }
        }
    }

    // MARK: - Вспомогательные методы
    private func isLocalDateToday(_ localDate: Kotlinx_datetimeLocalDate) -> Bool {
        // Конвертируем kotlinx.datetime.LocalDate в Foundation.Date
        let calendar = Calendar.current

        // Создаем DateComponents из LocalDate
        var dateComponents = DateComponents()
        dateComponents.year = Int(localDate.year)
        dateComponents.month = Int(localDate.monthNumber)
        dateComponents.day = Int(localDate.dayOfMonth)

        // Создаем Date из компонентов
        guard let date = calendar.date(from: dateComponents) else {
            return false
        }

        // Проверяем, сегодня ли эта дата
        return calendar.isDateInToday(date)
    }

    // MARK: - UNUserNotificationCenterDelegate
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
        DispatchQueue.main.async {
            (UIApplication.shared.delegate as? AppDelegate)?.showMainApp()
        }
        completionHandler()
    }
}