package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.dtos.NotificationResponseDTO;
import com.yourcompany.healthtracker.models.Notification;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.repositories.NotificationRepository;
import com.yourcompany.healthtracker.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final AuthenticationService authenticationService;

    // Lấy danh sách thông báo (có phân trang)
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        User user = authenticationService.getCurrentAuthenticatedUser();
        Page<Notification> pageResult = notificationRepository.findByUserOrderByCreatedAtDesc(
                user,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        return ResponseEntity.ok(
                pageResult.getContent().stream()
                        .map(NotificationResponseDTO::fromEntity)
                        .collect(Collectors.toList())
        );
    }

    // Đánh dấu 1 thông báo là đã đọc
    @PutMapping("/{id}/read")
    @Transactional
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        User user = authenticationService.getCurrentAuthenticatedUser();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Không có quyền truy cập");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
        return ResponseEntity.ok().build();
    }

    // Đánh dấu TẤT CẢ là đã đọc
    @PutMapping("/read-all")
    @Transactional
    public ResponseEntity<Void> markAllAsRead() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        // Cách tối ưu: Dùng @Query update trong Repository, nhưng ở đây dùng vòng lặp cho đơn giản logic demo
        // (Thực tế nên viết custom query update notifications set is_read=true where user_id=?)
        // ...
        return ResponseEntity.ok().build();
    }

    // Đếm số thông báo chưa đọc
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(notificationRepository.countByUserAndIsReadFalse(user));
    }
}