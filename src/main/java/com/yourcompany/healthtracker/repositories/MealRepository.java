package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.Meal;
import com.yourcompany.healthtracker.models.MealType;
import com.yourcompany.healthtracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    // Tìm bữa ăn cụ thể (ví dụ: Bữa trưa, hôm nay)
    Optional<Meal> findByUserAndDateAndMealType(User user, LocalDate date, MealType mealType);

    // Lấy tất cả bữa ăn trong ngày (để tổng hợp)
    List<Meal> findByUserAndDate(User user, LocalDate date);
}