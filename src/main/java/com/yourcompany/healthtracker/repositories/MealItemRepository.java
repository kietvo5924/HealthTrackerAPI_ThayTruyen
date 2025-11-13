package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.dtos.NutritionSummaryDTO;
import com.yourcompany.healthtracker.models.Meal;
import com.yourcompany.healthtracker.models.MealItem;
import com.yourcompany.healthtracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Long> {
    @Query("SELECT new com.yourcompany.healthtracker.dtos.NutritionSummaryDTO(" +
            "m.date, " +
            "SUM(mi.food.proteinGrams * mi.quantity), " +
            "SUM(mi.food.carbsGrams * mi.quantity), " +
            "SUM(mi.food.fatGrams * mi.quantity), " +
            "SUM(mi.food.calories * mi.quantity)) " +
            "FROM MealItem mi " +
            "JOIN mi.meal m " +
            // "mi.food" đã tự động được join, không cần "JOIN mi.food f"
            "WHERE m.user = :user AND m.date BETWEEN :startDate AND :endDate " +
            "GROUP BY m.date " +
            "ORDER BY m.date ASC")
    List<NutritionSummaryDTO> findNutritionSummaryByDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    void deleteByMeal(Meal meal);
}