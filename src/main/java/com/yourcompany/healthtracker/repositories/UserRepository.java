package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    /**
     * Tìm tất cả user có bật nhắc nhở đi ngủ.
     */
    List<User> findByRemindSleep(boolean remindSleep);

    /**
     * Tìm tất cả user có bật nhắc nhở uống nước.
     */
    List<User> findByRemindWater(boolean remindWater);
}
