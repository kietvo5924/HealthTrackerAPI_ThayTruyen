package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.MealItem;
import lombok.Data;

@Data
public class MealItemResponseDTO {
    private Long id;
    private Long foodId;
    private String foodName;
    private Double quantity;
    private String unit;
    private Double totalCalories;
    private Double totalProtein;
    private Double totalCarbs;
    private Double totalFat;

    public static MealItemResponseDTO fromEntity(MealItem item) {
        MealItemResponseDTO dto = new MealItemResponseDTO();
        dto.setId(item.getId());
        dto.setFoodId(item.getFood().getId());
        dto.setFoodName(item.getFood().getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnit(item.getFood().getUnit());

        // Tính toán tổng cộng dựa trên số lượng
        dto.setTotalCalories(item.getFood().getCalories() * item.getQuantity());
        dto.setTotalProtein(item.getFood().getProteinGrams() * item.getQuantity());
        dto.setTotalCarbs(item.getFood().getCarbsGrams() * item.getQuantity());
        dto.setTotalFat(item.getFood().getFatGrams() * item.getQuantity());
        return dto;
    }
}