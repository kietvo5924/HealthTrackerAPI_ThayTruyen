package com.yourcompany.healthtracker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "meals",
        uniqueConstraints = {
                // Đảm bảo mỗi user chỉ có 1 Bữa sáng, 1 Bữa trưa... mỗi ngày
                @UniqueConstraint(columnNames = {"user_id", "date", "meal_type"})
        })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Bữa ăn này của ai

    @Column(nullable = false)
    private LocalDate date; // Vào ngày nào

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType; // Là bữa sáng, trưa, hay tối?

    // Một bữa ăn (Meal) có nhiều Món (MealItem)
    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealItem> items = new java.util.ArrayList<>();
}