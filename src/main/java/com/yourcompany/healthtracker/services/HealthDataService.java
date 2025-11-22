package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.dtos.HealthDataLogRequest;
import com.yourcompany.healthtracker.models.HealthData;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.repositories.HealthDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthDataService {

    private final HealthDataRepository healthDataRepository;
    private final AuthenticationService authenticationService;
    private final GamificationService gamificationService;

    @Transactional
    public HealthData logOrUpdateHealthData(HealthDataLogRequest request) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        LocalDate date = (request.getDate() != null) ? request.getDate() : LocalDate.now();

        // Tìm hoặc tạo mới HealthData
        HealthData healthData = healthDataRepository.findByUserAndDate(currentUser, date)
                .orElseGet(() -> HealthData.builder()
                        .user(currentUser)
                        .date(date)
                        .steps(0)
                        .caloriesBurnt(0.0) // Tổng calo
                        .caloriesFromSteps(0.0) // Calo bước đi
                        .caloriesFromWorkout(0.0) // Calo workout
                        .sleepHours(0.0)
                        .waterIntake(0.0)
                        .build());

        // === LOGIC MỚI ===

        // 1. Xử lý Bước đi (GHI ĐÈ)
        if (request.getSteps() != null) {
            healthData.setSteps(request.getSteps());
            healthData.setCaloriesFromSteps(request.getSteps() * 0.04);

            gamificationService.checkStepAchievements(currentUser, request.getSteps());
        }

        // 2. Xử lý Calo Workout (CỘNG DỒN)
        // (Hàm này được WorkoutService gọi)
        if (request.getCaloriesBurnt() != null) {
            double currentWorkoutCals = (healthData.getCaloriesFromWorkout() != null)
                    ? healthData.getCaloriesFromWorkout() : 0.0;

            double newWorkoutCals = currentWorkoutCals + request.getCaloriesBurnt();
            healthData.setCaloriesFromWorkout(newWorkoutCals);

            log.info("Cập nhật calo WORKOUT: {} (cũ) + {} (mới) = {}",
                    currentWorkoutCals, request.getCaloriesBurnt(), newWorkoutCals);
        }

        // 3. Xử lý các trường khác (GHI ĐÈ)
        if (request.getSleepHours() != null) {
            healthData.setSleepHours(request.getSleepHours());
        }
        if (request.getWaterIntake() != null) {
            healthData.setWaterIntake(request.getWaterIntake());
        }
        if (request.getWeight() != null) {
            healthData.setWeight(request.getWeight());
        }

        // 4. Tính toán TỔNG calo
        // (Lấy giá trị hiện tại, nếu null thì dùng 0.0)
        double stepsCals = (healthData.getCaloriesFromSteps() != null) ? healthData.getCaloriesFromSteps() : 0.0;
        double workoutCals = (healthData.getCaloriesFromWorkout() != null) ? healthData.getCaloriesFromWorkout() : 0.0;
        healthData.setCaloriesBurnt(stepsCals + workoutCals);

        gamificationService.calculateAndSetDailyScore(healthData, currentUser);

        return healthDataRepository.save(healthData);
    }

    public HealthData getHealthDataForDate(LocalDate date) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        return healthDataRepository.findByUserAndDate(currentUser, date)
                .orElse(null); // Trả về null hoặc một DTO rỗng nếu không có dữ liệu
    }

    public List<HealthData> getHealthDataForDateRange(LocalDate startDate, LocalDate endDate) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        return healthDataRepository.findByUserAndDateBetween(currentUser, startDate, endDate);
    }
}