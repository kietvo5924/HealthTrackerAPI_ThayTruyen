package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    // Tìm kiếm thực phẩm theo tên (cho thanh search)
    List<Food> findByNameContainingIgnoreCase(String name);
}
