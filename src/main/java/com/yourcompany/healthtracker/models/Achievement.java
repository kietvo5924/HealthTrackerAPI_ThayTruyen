package com.yourcompany.healthtracker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "achievements")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // Mã định danh (ví dụ: STEPS_10K)

    private String name; // Tên hiển thị (ví dụ: Chiến binh đi bộ)
    private String description; // Mô tả
    private String iconUrl; // Link icon/ảnh huy hiệu
    private int targetValue; // Giá trị cần đạt (ví dụ: 10000)

    @Enumerated(EnumType.STRING)
    private AchievementType type; // Loại thành tựu

    public enum AchievementType {
        STEPS, WORKOUT_COUNT, CALORIES_BURNED
    }
}