package com.yourcompany.healthtracker.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "Đối tượng để cập nhật mục tiêu cá nhân")
public class UserGoalsDTO {

    @Schema(description = "Mục tiêu bước đi", example = "10000")
    @Min(value = 0, message = "Không thể là số âm")
    private Integer goalSteps;

    @Schema(description = "Mục tiêu nước (lít)", example = "2.5")
    @Min(value = 0, message = "Không thể là số âm")
    private Double goalWater;

    @Schema(description = "Mục tiêu ngủ (giờ)", example = "8.0")
    @Min(value = 0, message = "Không thể là số âm")
    private Double goalSleep;

    @Schema(description = "Mục tiêu calo vận động", example = "500")
    @Min(value = 0, message = "Không thể là số âm")
    private Integer goalCaloriesBurnt;

    @Schema(description = "Mục tiêu calo nạp vào", example = "2000")
    @Min(value = 0, message = "Không thể là số âm")
    private Integer goalCaloriesConsumed;
}