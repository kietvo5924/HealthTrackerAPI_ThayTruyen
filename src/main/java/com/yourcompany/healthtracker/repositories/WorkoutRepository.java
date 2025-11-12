package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    // Lấy tất cả bài tập của 1 user, sắp xếp theo ngày mới nhất
    List<Workout> findByUserOrderByStartedAtDesc(User user);

    // Lấy 20 bài tập mới nhất từ tất cả user
    // (Spring Data JPA sẽ tự động join với User khi chúng ta gọi .getUser().getFullName())
    List<Workout> findTop20ByOrderByStartedAtDesc();
}