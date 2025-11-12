package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.Workout;
import com.yourcompany.healthtracker.models.WorkoutType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class WorkoutResponseDTO {

    private Long id;
    private WorkoutType workoutType;
    private Integer durationInMinutes;
    private Double caloriesBurned;
    private OffsetDateTime startedAt;
    private Double distanceInKm;
    private String routePolyline;

    @Schema(description = "Tên đầy đủ của người thực hiện bài tập", example = "Nguyễn Văn A")
    private String userFullName;

    // Hàm chuyển đổi từ Entity (Model) sang DTO
    public static WorkoutResponseDTO fromEntity(Workout workout) {
        return WorkoutResponseDTO.builder()
                .id(workout.getId())
                .workoutType(workout.getWorkoutType())
                .durationInMinutes(workout.getDurationInMinutes())
                .caloriesBurned(workout.getCaloriesBurned())
                .startedAt(workout.getStartedAt())
                .distanceInKm(workout.getDistanceInKm())
                .routePolyline(workout.getRoutePolyline())
                .userFullName(workout.getUser().getFullName())
                .build();
    }
}