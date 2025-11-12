package com.yourcompany.healthtracker.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Schema(description = "Đối tượng để tạo một món ăn (food) mới")
public class FoodCreateRequestDTO {

    @NotBlank(message = "Tên món ăn là bắt buộc")
    @Schema(description = "Tên thực phẩm", example = "Cơm sườn bì chả")
    private String name;

    @NotBlank(message = "Đơn vị là bắt buộc")
    @Schema(description = "Đơn vị tính", example = "dĩa")
    private String unit;

    @NotNull(message = "Calo là bắt buộc")
    @PositiveOrZero(message = "Calo không được âm")
    @Schema(description = "Calo trên 1 đơn vị", example = "550")
    private Double calories;

    @NotNull(message = "Protein là bắt buộc")
    @PositiveOrZero(message = "Protein không được âm")
    @Schema(description = "Protein (g) trên 1 đơn vị", example = "30")
    private Double proteinGrams;

    @NotNull(message = "Carb là bắt buộc")
    @PositiveOrZero(message = "Carb không được âm")
    @Schema(description = "Carb (g) trên 1 đơn vị", example = "60")
    private Double carbsGrams;

    @NotNull(message = "Fat là bắt buộc")
    @PositiveOrZero(message = "Fat không được âm")
    @Schema(description = "Fat (g) trên 1 đơn vị", example = "20")
    private Double fatGrams;
}