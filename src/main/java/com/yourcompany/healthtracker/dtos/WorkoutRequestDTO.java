package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.WorkoutType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Schema(description = "Đối tượng để ghi (log) một bài tập mới")
public class WorkoutRequestDTO {

    @Schema(description = "Loại bài tập", example = "RUNNING")
    @NotNull(message = "Loại bài tập là bắt buộc")
    private WorkoutType workoutType;

    @Schema(description = "Thời lượng (phút)", example = "30")
    @NotNull(message = "Thời lượng là bắt buộc")
    @PositiveOrZero(message = "Thời lượng phải lớn hơn hoặc bằng 0")
    private Integer durationInMinutes;

    @Schema(description = "Calo đã đốt (tùy chọn)", example = "350.5")
    private Double caloriesBurned;

    @Schema(description = "Thời điểm bắt đầu (ISO 8601)", example = "2025-11-12T10:00:00+07:00")
    @NotNull(message = "Thời điểm bắt đầu là bắt buộc")
    private OffsetDateTime startedAt;

    @Schema(description = "Quãng đường (km), tùy chọn", example = "5.2")
    private Double distanceInKm;

    @Schema(description = "Chuỗi polyline của tuyến đường (tùy chọn)")
    private String routePolyline;
}