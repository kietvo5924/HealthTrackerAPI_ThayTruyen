package com.yourcompany.healthtracker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "health_data",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "date"}) // Đảm bảo mỗi user chỉ có 1 bản ghi/ngày
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Quan hệ nhiều-một với User
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date; // Ngày của bản ghi

    private Integer steps; // Số bước đi

    private Double caloriesBurnt; // Calo đã đốt

    private Double sleepHours; // Số giờ ngủ

    private Double waterIntake; // Lượng nước uống (lít)

    private Double weight; // Cân nặng (kg)

    // Bạn có thể thêm các chỉ số khác như nhịp tim,...
}
