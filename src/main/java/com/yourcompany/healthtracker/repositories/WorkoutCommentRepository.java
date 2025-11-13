package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.Workout;
import com.yourcompany.healthtracker.models.WorkoutComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutCommentRepository extends JpaRepository<WorkoutComment, Long> {
    // Tìm tất cả bình luận cho một bài tập, sắp xếp theo thời gian
    List<WorkoutComment> findByWorkoutOrderByCreatedAtAsc(Workout workout);
}