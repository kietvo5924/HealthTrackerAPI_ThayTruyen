package com.yourcompany.healthtracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSummaryDTO {
    private LocalDate date;
    private Long totalDurationInMinutes;
    private Double totalCaloriesBurned;
    private Double totalDistanceInKm;
}