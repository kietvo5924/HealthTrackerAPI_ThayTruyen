package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.UserGoals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGoalsRepository extends JpaRepository<UserGoals, Long> {
}