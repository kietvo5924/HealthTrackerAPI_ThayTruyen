package com.yourcompany.healthtracker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;

    // Loại thông báo để Frontend hiện icon tương ứng (ví dụ: SYSTEM, SOCIAL, ACHIEVEMENT)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private boolean isRead; // Đã đọc hay chưa

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Thông báo của ai

    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        isRead = false;
    }

    public enum NotificationType {
        SYSTEM, SOCIAL, ACHIEVEMENT, REMINDER
    }
}