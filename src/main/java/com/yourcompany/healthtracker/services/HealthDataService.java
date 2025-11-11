package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.dtos.HealthDataLogRequest;
import com.yourcompany.healthtracker.models.HealthData;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.repositories.HealthDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HealthDataService {

    private final HealthDataRepository healthDataRepository;
    private final AuthenticationService authenticationService; // Để lấy user đang đăng nhập

    @Transactional
    public HealthData logOrUpdateHealthData(HealthDataLogRequest request) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        LocalDate date = (request.getDate() != null) ? request.getDate() : LocalDate.now();

        // Tìm xem đã có bản ghi cho ngày hôm nay chưa
        HealthData healthData = healthDataRepository.findByUserAndDate(currentUser, date)
                .orElseGet(() -> HealthData.builder() // Nếu chưa có, tạo mới
                        .user(currentUser)
                        .date(date)
                        .build());

        // Cập nhật các trường nếu chúng được cung cấp trong request
        if (request.getSteps() != null) {
            healthData.setSteps(request.getSteps());
        }
        if (request.getCaloriesBurnt() != null) {
            healthData.setCaloriesBurnt(request.getCaloriesBurnt());
        }
        if (request.getSleepHours() != null) {
            healthData.setSleepHours(request.getSleepHours());
        }
        if (request.getWaterIntake() != null) {
            healthData.setWaterIntake(request.getWaterIntake());
        }
        if (request.getWeight() != null) {
            healthData.setWeight(request.getWeight());
        }

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