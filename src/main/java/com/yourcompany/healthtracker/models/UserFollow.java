package com.yourcompany.healthtracker.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_follows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"follower_id", "following_id"}) // Đảm bảo không follow trùng
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người đi theo dõi (Me)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    // Người được theo dõi (Friend)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
    }
}