package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.dtos.AddMealItemRequestDTO;
import com.yourcompany.healthtracker.dtos.FoodCreateRequestDTO;
import com.yourcompany.healthtracker.dtos.FoodResponseDTO;
import com.yourcompany.healthtracker.dtos.MealResponseDTO;
import com.yourcompany.healthtracker.services.NutritionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/nutrition") // Đặt base URL là /api/nutrition
@RequiredArgsConstructor
@Tag(name = "Nutrition API", description = "APIs để theo dõi Dinh dưỡng")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
public class NutritionController {

    private final NutritionService nutritionService;

    @Operation(summary = "Tìm kiếm thực phẩm",
            description = "Tìm kiếm thư viện thực phẩm theo tên. Ví dụ: /api/nutrition/food?query=chuối")
    @GetMapping("/food")
    public ResponseEntity<List<FoodResponseDTO>> searchFood(
            @RequestParam(required = false, defaultValue = "") String query
    ) {
        List<FoodResponseDTO> foods = nutritionService.searchFood(query);
        return ResponseEntity.ok(foods);
    }

    @Operation(summary = "Tạo một món ăn (Food) mới",
            description = "Cho phép người dùng thêm món ăn của riêng họ vào CSDL.")
    @PostMapping("/food")
    public ResponseEntity<FoodResponseDTO> createFood(
            @Valid @RequestBody FoodCreateRequestDTO request
    ) {
        FoodResponseDTO newFood = nutritionService.createFood(request);
        // Trả về 201 Created (thay vì 200 OK)
        return ResponseEntity.status(201).body(newFood);
    }

    @Operation(summary = "Lấy các bữa ăn theo ngày",
            description = "Lấy tất cả các bữa ăn (Sáng, Trưa, Tối, Phụ) và các món trong đó của một ngày. " +
                    "Ví dụ: /api/nutrition/meals?date=2025-11-12")
    @GetMapping("/meals")
    public ResponseEntity<List<MealResponseDTO>> getMealsForDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<MealResponseDTO> meals = nutritionService.getMealsForDate(date);
        return ResponseEntity.ok(meals);
    }

    @Operation(summary = "Thêm một món ăn vào bữa ăn",
            description = "Thêm một thực phẩm (food) vào một bữa ăn (meal). " +
                    "Hệ thống sẽ tự động tạo bữa ăn nếu nó chưa tồn tại.")
    @PostMapping("/meals/item")
    public ResponseEntity<MealResponseDTO> addFoodToMeal(
            @Valid @RequestBody AddMealItemRequestDTO request
    ) {
        MealResponseDTO updatedMeal = nutritionService.addFoodToMeal(request);
        return ResponseEntity.ok(updatedMeal);
    }

    @Operation(summary = "Xóa một món ăn khỏi bữa ăn",
            description = "Xóa một MealItem (món đã thêm) bằng ID của nó.")
    @DeleteMapping("/meals/item/{itemId}")
    public ResponseEntity<Void> deleteMealItem(
            @PathVariable Long itemId
    ) {
        nutritionService.deleteMealItem(itemId);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content
    }
}
