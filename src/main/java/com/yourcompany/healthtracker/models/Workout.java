package com.yourcompany.healthtracker.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Entity
@Table(name = "workouts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    private WorkoutType workoutType;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationInMinutes; // Thời lượng (phút)

    @Column(name = "calories_burned")
    private Double caloriesBurned; // Calo đã đốt

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt; // Thời điểm bắt đầu

    @Column(name = "distance_km")
    private Double distanceInKm; // Quãng đường (km)

    @Column(name = "route_polyline", columnDefinition = "TEXT")
    private String routePolyline; // Chuỗi toạ độ (nếu có)

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<WorkoutLike> likes = new java.util.ArrayList<>();
}