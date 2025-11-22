package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.models.HealthData;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.UserGoals;
import com.yourcompany.healthtracker.repositories.UserGoalsRepository;
import com.yourcompany.healthtracker.repositories.UserRepository;
import com.yourcompany.healthtracker.repositories.HealthDataRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final UserRepository userRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    private final HealthDataRepository healthDataRepository;
    private final UserGoalsRepository userGoalsRepository;

    /**
     * Chạy tác vụ vào 12:00 trưa hàng ngày.
     */
    @Scheduled(cron = "0 0 10,14,16 * * ?") // Chạy 3 lần/ngày thay vì 1 lần
    public void sendSmartWaterReminder() {
        log.info("--- Bắt đầu Job nhắc nước thông minh ---");
        LocalDate today = LocalDate.now();
        List<User> users = userRepository.findByRemindWater(true);

        for (User user : users) {
            if (user.getFcmToken() == null || user.getFcmToken().isEmpty()) continue;

            // 1. Lấy mục tiêu nước của User (Mặc định 2.5L nếu chưa set)
            UserGoals goals = userGoalsRepository.findByUserId(user.getId()).orElse(null);
            double targetWater = (goals != null && goals.getGoalWater() != null) ? goals.getGoalWater() : 2.5;

            // 2. Lấy lượng nước đã uống hôm nay
            Optional<HealthData> healthDataOpt = healthDataRepository.findByUserIdAndDate(user.getId(), today);
            double currentWater = healthDataOpt.map(HealthData::getWaterIntake).orElse(0.0);

            // 3. So sánh và gửi thông báo cá nhân hóa
            if (currentWater < targetWater) {
                double remaining = targetWater - currentWater;
                String message;

                // Logic tạo câu thông báo "thân thiện"
                if (currentWater == 0) {
                    message = "💧 Hôm nay bạn chưa uống chút nước nào. Hãy khởi động ngày mới với 1 ly nước nhé!";
                } else if (currentWater < targetWater * 0.5) {
                    message = String.format("💧 Bạn mới hoàn thành %.0f%% mục tiêu nước. Hãy uống thêm %.1f lít nữa nhé!",
                            (currentWater/targetWater)*100, remaining);
                } else {
                    message = String.format("💪 Sắp hoàn thành rồi! Chỉ còn %.1f lít nước nữa là đạt mục tiêu hôm nay.", remaining);
                }

                firebaseMessagingService.sendNotification(user.getFcmToken(), "Nhắc nhở uống nước", message);
                log.info("Đã gửi nhắc user {}: {}", user.getId(), message);
            }
        }
    }

    /**
     * Nhắc nhở VẬN ĐỘNG (Nếu cả ngày ngồi im)
     * Chạy vào: 17:30 chiều
     */
    @Scheduled(cron = "0 30 17 * * ?")
    public void sendMovementReminder() {
        log.info("--- Bắt đầu Job nhắc vận động ---");
        LocalDate today = LocalDate.now();
        List<User> users = userRepository.findAll(); // Hoặc lọc theo setting nếu có

        for (User user : users) {
            if (user.getFcmToken() == null || user.getFcmToken().isEmpty()) continue;

            // Lấy số bước chân hôm nay
            Optional<HealthData> data = healthDataRepository.findByUserIdAndDate(user.getId(), today);
            int steps = data.map(HealthData::getSteps).orElse(0);

            // Nếu gần tối mà đi dưới 2000 bước -> Nhắc nhở
            if (steps < 2000) {
                String msg = "🏃 Bạn ngồi hơi lâu rồi đấy! Hãy đứng dậy đi lại vài vòng để thư giãn nhé.";
                firebaseMessagingService.sendNotification(user.getFcmToken(), "Vận động một chút nào!", msg);
            }
        }
    }

    /**
     * Nhắc nhở ĐI NGỦ (Giữ nguyên logic cũ nhưng đổi nội dung)
     * Chạy vào: 22:00 tối
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void sendSleepReminder() {
        List<User> users = userRepository.findByRemindSleep(true);
        for (User user : users) {
            if (user.getFcmToken() != null) {
                firebaseMessagingService.sendNotification(
                        user.getFcmToken(),
                        "Đã đến giờ ngủ 😴",
                        "Ngủ đủ giấc giúp tái tạo năng lượng. Hãy đặt điện thoại xuống và nghỉ ngơi nhé!"
                );
            }
        }
    }
}