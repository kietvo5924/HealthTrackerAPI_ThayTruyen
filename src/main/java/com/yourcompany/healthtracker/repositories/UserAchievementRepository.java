package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    List<UserAchievement> findByUser(User user);
    boolean existsByUserAndAchievementId(User user, Long achievementId);
}