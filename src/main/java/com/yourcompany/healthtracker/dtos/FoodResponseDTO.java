package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.Food;
import lombok.Data;

@Data
public class FoodResponseDTO {
    private Long id;
    private String name;
    private String unit;
    private Double calories;
    private Double proteinGrams;
    private Double carbsGrams;
    private Double fatGrams;

    // Chuyển đổi từ Entity (Model) sang DTO
    public static FoodResponseDTO fromEntity(Food food) {
        FoodResponseDTO dto = new FoodResponseDTO();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setUnit(food.getUnit());
        dto.setCalories(food.getCalories());
        dto.setProteinGrams(food.getProteinGrams());
        dto.setCarbsGrams(food.getCarbsGrams());
        dto.setFatGrams(food.getFatGrams());
        return dto;
    }
}