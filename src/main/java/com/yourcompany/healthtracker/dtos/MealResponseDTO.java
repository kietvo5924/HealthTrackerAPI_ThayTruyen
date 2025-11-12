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
        if (meal.getItems() != null) {
            dto.setItems(meal.getItems().stream()
                    .map(MealItemResponseDTO::fromEntity)
                    .collect(Collectors.toList()));
        } else {
            dto.setItems(new java.util.ArrayList<>()); // Trả về một danh sách rỗng
        }

        // Tính tổng calo cho bữa ăn
        dto.setTotalMealCalories(dto.getItems().stream()
                .mapToDouble(MealItemResponseDTO::getTotalCalories)
                .sum());

        return dto;
    }
}