package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.Meal;
import com.yourcompany.healthtracker.models.MealType;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MealResponseDTO {
    private Long id;
    private MealType mealType;
    private List<MealItemResponseDTO> items;
    private Double totalMealCalories;

    public static MealResponseDTO fromEntity(Meal meal) {
        MealResponseDTO dto = new MealResponseDTO();
        dto.setId(meal.getId());
        dto.setMealType(meal.getMealType());

        // Chuyển đổi List<MealItem> thành List<MealItemResponseDTO>
        dto.setItems(meal.getItems().stream()
                .map(MealItemResponseDTO::fromEntity)
                .collect(Collectors.toList()));

        // Tính tổng calo cho bữa ăn
        dto.setTotalMealCalories(dto.getItems().stream()
                .mapToDouble(MealItemResponseDTO::getTotalCalories)
                .sum());

        return dto;
    }
}