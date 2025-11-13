package com.yourcompany.healthtracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionSummaryDTO {
    private LocalDate date;
    private Double totalProtein;
    private Double totalCarbs;
    private Double totalFat;
    private Double totalCalories;
}