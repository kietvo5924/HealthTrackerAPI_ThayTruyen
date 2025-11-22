package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.HealthData;
import com.yourcompany.healthtracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthDataRepository extends JpaRepository<HealthData, Long> {

    // Tìm bản ghi theo user và một ngày cụ thể
    Optional<HealthData> findByUserAndDate(User user, LocalDate date);

    // Tìm bản ghi theo user trong một khoảng thời gian (cho biểu đồ)
    List<HealthData> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    boolean existsByUserAndDateAndWaterIntakeGreaterThan(User user, LocalDate date, Double waterIntake);

    Optional<HealthData> findByUserIdAndDate(Long userId, LocalDate date);
}