package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.models.*;
import com.yourcompany.healthtracker.repositories.AchievementRepository;
import com.yourcompany.healthtracker.repositories.UserAchievementRepository;
import com.yourcompany.healthtracker.repositories.UserGoalsRepository;
import com.yourcompany.healthtracker.repositories.WorkoutRepository; // Inject thêm cái này
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamificationService {

    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final WorkoutRepository workoutRepository; // Để đếm số bài tập
    private final FirebaseMessagingService firebaseMessagingService;
    private final UserGoalsRepository userGoalsRepository;

    // 1. Kiểm tra Bước chân (Đã có)
    @Transactional
    public void checkStepAchievements(User user, int currentSteps) {
        List<Achievement> achievements = achievementRepository.findByType(Achievement.AchievementType.STEPS);
        for (Achievement a : achievements) {
            if (currentSteps >= a.getTargetValue()) unlockAchievement(user, a);
        }
    }

    // 2. Kiểm tra Số lượng bài tập (MỚI)
    @Transactional
    public void checkWorkoutCountAchievements(User user) {
        long workoutCount = workoutRepository.countByUser(user);
        List<Achievement> achievements = achievementRepository.findByType(Achievement.AchievementType.WORKOUT_COUNT);

        for (Achievement a : achievements) {
            if (workoutCount >= a.getTargetValue()) unlockAchievement(user, a);
        }
    }

    // 3. Kiểm tra Calo trong 1 buổi (MỚI)
    @Transactional
    public void checkCalorieAchievements(User user, int caloriesInSession) {
        List<Achievement> achievements = achievementRepository.findByType(Achievement.AchievementType.CALORIES_BURNED);

        for (Achievement a : achievements) {
            if (caloriesInSession >= a.getTargetValue()) unlockAchievement(user, a);
        }
    }

    private void unlockAchievement(User user, Achievement achievement) {
        if (userAchievementRepository.existsByUserAndAchievementId(user, achievement.getId())) {
            return; // Đã nhận rồi thì bỏ qua
        }

        UserAchievement ua = UserAchievement.builder().user(user).achievement(achievement).build();
        userAchievementRepository.save(ua);

        // Gửi thông báo
        if (user.getFcmToken() != null) {
            firebaseMessagingService.sendNotificationToUser(
                    user,
                    "🏆 Thành tựu mới!",
                    "Bạn vừa đạt danh hiệu: " + achievement.getName(),
                    Notification.NotificationType.ACHIEVEMENT
            );
        }
    }

    public List<UserAchievement> getMyAchievements(User user) {
        return userAchievementRepository.findByUser(user);
    }

    /**
     * Tính toán và cập nhật điểm sức khỏe (0 - 100) cho bản ghi HealthData
     */
    public void calculateAndSetDailyScore(HealthData data, User user) {
        // 1. Lấy mục tiêu của người dùng (Nếu chưa có thì lấy mặc định)
        UserGoals goals = userGoalsRepository.findByUserId(user.getId())
                .orElse(UserGoals.builder()
                        .goalSteps(10000)
                        .goalWater(2.0)
                        .goalSleep(8.0)
                        .build());

        int score = 0;

        // 2. Tính điểm Bước chân (Trọng số 40%)
        // Nếu đi > mục tiêu thì vẫn chỉ được tối đa 40 điểm
        double stepProgress = (double) data.getSteps() / goals.getGoalSteps();
        if (stepProgress > 1.0) stepProgress = 1.0;
        score += (int) (stepProgress * 40);

        // 3. Tính điểm Nước (Trọng số 30%)
        double waterProgress = data.getWaterIntake() / goals.getGoalWater();
        if (waterProgress > 1.0) waterProgress = 1.0;
        score += (int) (waterProgress * 30);

        // 4. Tính điểm Ngủ (Trọng số 30%)
        // Chỉ tính nếu đã có dữ liệu ngủ
        if (data.getSleepHours() != null && data.getSleepHours() > 0) {
            double sleepProgress = data.getSleepHours() / goals.getGoalSleep();
            if (sleepProgress > 1.0) sleepProgress = 1.0;
            score += (int) (sleepProgress * 30);
        }

        // 5. Cập nhật vào data
        data.setDailyScore(score);
    }
}