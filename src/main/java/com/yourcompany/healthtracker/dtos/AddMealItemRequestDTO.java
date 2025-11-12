package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.MealType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AddMealItemRequestDTO {

    @NotNull(message = "Food ID là bắt buộc")
    private Long foodId;

    @NotNull(message = "Ngày là bắt buộc")
    private LocalDate date;

    @NotNull(message = "Loại bữa ăn là bắt buộc")
    private MealType mealType;

    @NotNull(message = "Số lượng là bắt buộc")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Double quantity;
}