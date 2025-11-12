package com.yourcompany.healthtracker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "foods")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Tên thực phẩm, ví dụ: "Ức gà phi lê"

    @Column(nullable = false)
    private String unit; // Đơn vị, ví dụ: "gram", "ml", "chén"

    @Column(nullable = false)
    private Double calories; // Calo trên 1 đơn vị

    @Column(nullable = false)
    private Double proteinGrams; // Protein trên 1 đơn vị

    @Column(nullable = false)
    private Double carbsGrams; // Carb trên 1 đơn vị

    @Column(nullable = false)
    private Double fatGrams; // Fat trên 1 đơn vị

    // (Nâng cao) Chúng ta có thể thêm 1 liên kết tới User
    // để biết món này là "chung" (null) hay do user tự tạo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_user_id")
    private User addedByUser;
}