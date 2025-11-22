package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.Achievement;
import com.yourcompany.healthtracker.models.Achievement.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByCode(String code);
    List<Achievement> findByType(AchievementType type);
}