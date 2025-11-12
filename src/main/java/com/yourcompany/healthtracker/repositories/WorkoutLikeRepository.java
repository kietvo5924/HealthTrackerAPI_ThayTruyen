package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.Workout;
import com.yourcompany.healthtracker.models.WorkoutLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutLikeRepository extends JpaRepository<WorkoutLike, Long> {

    // Tìm like dựa trên user và workout
    Optional<WorkoutLike> findByUserAndWorkout(User user, Workout workout);

    // Đếm số like cho 1 workout
    long countByWorkout(Workout workout);
}