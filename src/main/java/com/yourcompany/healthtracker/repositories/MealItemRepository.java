package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Long> {
    // (Chúng ta có thể không cần query phức tạp ở đây)
}