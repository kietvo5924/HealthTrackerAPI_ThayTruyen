package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.models.Achievement;
import com.yourcompany.healthtracker.models.Notification;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.UserAchievement;
import com.yourcompany.healthtracker.repositories.AchievementRepository;
import com.yourcompany.healthtracker.repositories.UserAchievementRepository;
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
}