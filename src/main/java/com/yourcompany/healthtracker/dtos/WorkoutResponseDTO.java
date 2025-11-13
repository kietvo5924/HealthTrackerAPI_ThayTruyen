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

    @Schema(description = "Tổng số lượt thích", example = "10")
    private long likeCount;
    @Schema(description = "Cho biết user hiện tại đã thích bài này hay chưa", example = "true")
    private boolean likedByCurrentUser;
    @Schema(description = "Tổng số bình luận", example = "5")
    private long commentCount;

    // Hàm chuyển đổi từ Entity (Model) sang DTO
    public static WorkoutResponseDTO fromEntity(Workout workout, Long currentUserId) {
        long count = 0;
        boolean liked = false;
        long commentCount = 0;

        // Kiểm tra null an toàn (vì đã khởi tạo = new ArrayList ở Entity)
        if (workout.getLikes() != null) {
            count = workout.getLikes().size();
            liked = workout.getLikes().stream()
                    .anyMatch(like -> like.getUser() != null && like.getUser().getId().equals(currentUserId));
        }

        if (workout.getComments() != null) {
            commentCount = workout.getComments().size();
        }

        return WorkoutResponseDTO.builder()
                .id(workout.getId())
                .workoutType(workout.getWorkoutType())
                .durationInMinutes(workout.getDurationInMinutes())
                .caloriesBurned(workout.getCaloriesBurned())
                .startedAt(workout.getStartedAt())
                .distanceInKm(workout.getDistanceInKm())
                .routePolyline(workout.getRoutePolyline())
                .userFullName(workout.getUser().getFullName())
                .likeCount(count)
                .likedByCurrentUser(liked)
                .commentCount(commentCount)
                .build();
    }
}