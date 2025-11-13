package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.dtos.WorkoutSummaryDTO;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    // Lấy tất cả bài tập của 1 user, sắp xếp theo ngày mới nhất
    List<Workout> findByUserOrderByStartedAtDesc(User user);

    // Lấy 20 bài tập mới nhất từ tất cả user
    // (Spring Data JPA sẽ tự động join với User khi chúng ta gọi .getUser().getFullName())
    List<Workout> findTop20ByOrderByStartedAtDesc();

    @Query("SELECT new com.yourcompany.healthtracker.dtos.WorkoutSummaryDTO(" +
            "CAST(w.startedAt AS java.time.LocalDate), " +
            "SUM(w.durationInMinutes), " +
            "SUM(w.caloriesBurned), " +
            "SUM(w.distanceInKm)) " +
            "FROM Workout w " +
            "WHERE w.user = :user AND CAST(w.startedAt AS java.time.LocalDate) BETWEEN :startDate AND :endDate " +
            "GROUP BY CAST(w.startedAt AS java.time.LocalDate) " +
            "ORDER BY CAST(w.startedAt AS java.time.LocalDate) ASC")
    List<WorkoutSummaryDTO> findWorkoutSummaryByDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}