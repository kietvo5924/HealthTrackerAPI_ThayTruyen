package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.Notification;
import com.yourcompany.healthtracker.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Lấy thông báo của user, sắp xếp mới nhất trước
    Page<Notification> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // Đếm số thông báo chưa đọc (để hiện chấm đỏ trên icon chuông)
    long countByUserAndIsReadFalse(User user);
}