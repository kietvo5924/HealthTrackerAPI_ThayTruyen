package com.yourcompany.healthtracker.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "Đối tượng để ghi (log) các chỉ số sức khỏe. " +
        "Tất cả các chỉ số đều là tùy chọn. " +
        "Nếu không cung cấp 'date', hệ thống sẽ mặc định là hôm nay.")
public class HealthDataLogRequest {

    @Schema(description = "Ngày của bản ghi, ví dụ: 2025-11-10. Mặc định là hôm nay.")
    private LocalDate date;

    @Schema(description = "Số bước đi")
    private Integer steps;

    @Schema(description = "Lượng calo đã đốt")
    private Double caloriesBurnt;

    @Schema(description = "Số giờ ngủ")
    private Double sleepHours;

    @Schema(description = "Lượng nước đã uống (lít)")
    private Double waterIntake;

    @Schema(description = "Cân nặng (kg)")
    private Double weight;
}