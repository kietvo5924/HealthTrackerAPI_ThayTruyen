package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.repositories.UserRepository;
import com.yourcompany.healthtracker.repositories.HealthDataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final UserRepository userRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    private final HealthDataRepository healthDataRepository; // <-- Cần repository này

    /**
     * Chạy tác vụ vào 12:00 trưa hàng ngày.
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void sendWaterReminder() {
        log.info("Chạy job nhắc nhở uống nước...");
        LocalDate today = LocalDate.now();

        // Tìm user BẬT nhắc nhở và CHƯA log nước hôm nay
        // Chúng ta sẽ dùng HealthDataRepository để kiểm tra
        List<User> usersToRemind = userRepository.findByRemindWater(true);

        for (User user : usersToRemind) {
            // Kiểm tra xem user đã log nước > 0 hôm nay chưa
            boolean hasLoggedWater = healthDataRepository.existsByUserAndDateAndWaterIntakeGreaterThan(user, today, 0.0);

            if (!hasLoggedWater && user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                log.info("Gửi nhắc nhở uống nước cho user: {}", user.getId());
                firebaseMessagingService.sendNotification(
                        user.getFcmToken(),
                        "Nhắc nhở uống nước 💧",
                        "Bạn ơi, hôm nay bạn chưa uống nước. Hãy cập nhật nhé!"
                );
            }
        }
        log.info("Kết thúc job nhắc nhở uống nước.");
    }

    /**
     * Chạy tác vụ vào 9:00 tối (21:00) hàng ngày.
     */
    @Scheduled(cron = "0 0 21 * * ?")
    public void sendSleepReminder() {
        log.info("Chạy job nhắc nhở đi ngủ...");

        // Tìm tất cả user BẬT nhắc nhở ngủ
        List<User> usersToRemind = userRepository.findByRemindSleep(true);

        for (User user : usersToRemind) {
            if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                log.info("Gửi nhắc nhở đi ngủ cho user: {}", user.getId());
                firebaseMessagingService.sendNotification(
                        user.getFcmToken(),
                        "Chúc ngủ ngon 😴",
                        "Đã đến giờ đi ngủ. Hãy nghỉ ngơi để đảm bảo sức khỏe nhé!"
                );
            }
        }
        log.info("Kết thúc job nhắc nhở đi ngủ.");
    }
}