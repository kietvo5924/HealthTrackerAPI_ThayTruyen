package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.dtos.*;
import com.yourcompany.healthtracker.models.*;
import com.yourcompany.healthtracker.repositories.FoodRepository;
import com.yourcompany.healthtracker.repositories.MealItemRepository;
import com.yourcompany.healthtracker.repositories.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;
    private final MealItemRepository mealItemRepository;
    private final AuthenticationService authenticationService; // Để lấy user

    /**
     * Tìm kiếm thực phẩm trong CSDL
     */
    public List<FoodResponseDTO> searchFood(String query) {
        List<Food> foods;
        if (query.isEmpty()) {
            // Nếu query rỗng, lấy tất cả món ăn
            foods = foodRepository.findAll();
        } else {
            // Nếu có query, tìm theo tên
            foods = foodRepository.findByNameContainingIgnoreCase(query);
        }

        return foods.stream()
                .map(FoodResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Lấy tất cả bữa ăn (và món ăn) của user trong một ngày
     */
    public List<MealResponseDTO> getMealsForDate(LocalDate date) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        return mealRepository.findByUserAndDate(currentUser, date)
                .stream()
                .map(MealResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Thêm một món ăn vào bữa ăn.
     * Tự động tìm hoặc tạo "Bữa ăn" (ví dụ: Bữa trưa) nếu chưa có.
     */
    @Transactional
    public MealResponseDTO addFoodToMeal(AddMealItemRequestDTO request) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();

        // 1. Tìm thực phẩm (Food)
        Food food = foodRepository.findById(request.getFoodId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thực phẩm với ID: " + request.getFoodId()));

        // 2. Tìm hoặc Tạo Bữa ăn (Meal)
        Meal meal = mealRepository.findByUserAndDateAndMealType(
                        currentUser, request.getDate(), request.getMealType())
                .orElseGet(() -> {
                    // Nếu không có, tạo mới
                    Meal newMeal = Meal.builder()
                            .user(currentUser)
                            .date(request.getDate())
                            .mealType(request.getMealType())
                            .build();
                    return mealRepository.save(newMeal);
                });

        // 3. Tạo Món ăn (MealItem) mới
        MealItem mealItem = MealItem.builder()
                .meal(meal)
                .food(food)
                .quantity(request.getQuantity())
                .build();

        mealItemRepository.save(mealItem);

        // 4. Tải lại bữa ăn (Meal) đã được cập nhật
        Meal updatedMeal = mealRepository.findById(meal.getId())
                .orElseThrow(() -> new RuntimeException("Lỗi khi tải lại bữa ăn"));

        return MealResponseDTO.fromEntity(updatedMeal);
    }

    /**
     * Xóa một món ăn (MealItem) khỏi bữa ăn
     */
    @Transactional
    public void deleteMealItem(Long mealItemId) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();

        // 1. Tìm món ăn (MealItem)
        MealItem mealItem = mealItemRepository.findById(mealItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn với ID: " + mealItemId));

        // 2. Xác thực: Món ăn này có thuộc về user đang đăng nhập không?
        if (!mealItem.getMeal().getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Bạn không có quyền xóa món ăn này.");
        }

        // 3. Xóa
        mealItemRepository.delete(mealItem);
    }

    /**
     * Tạo một món ăn (Food) mới, do người dùng định nghĩa
     */
    @Transactional
    public FoodResponseDTO createFood(FoodCreateRequestDTO request) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();

        Food newFood = Food.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .calories(request.getCalories())
                .proteinGrams(request.getProteinGrams())
                .carbsGrams(request.getCarbsGrams())
                .fatGrams(request.getFatGrams())
                .addedByUser(currentUser) // Đánh dấu món ăn này là của user
                .build();

        Food savedFood = foodRepository.save(newFood);

        return FoodResponseDTO.fromEntity(savedFood);
    }

    public List<NutritionSummaryDTO> getNutritionSummary(LocalDate startDate, LocalDate endDate) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        return mealItemRepository.findNutritionSummaryByDateRange(currentUser, startDate, endDate);
    }
}