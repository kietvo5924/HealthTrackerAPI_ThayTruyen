package com.yourcompany.healthtracker.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_goals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGoals {

    @Id
    private Long id; // Dùng chung ID với User

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Đánh dấu đây là khóa ngoại và cũng là khóa chính
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column(name = "goal_steps", columnDefinition = "integer default 10000")
    @Builder.Default
    private Integer goalSteps = 10000;

    @Column(name = "goal_water", columnDefinition = "double precision default 2.5")
    @Builder.Default
    private Double goalWater = 2.5;

    @Column(name = "goal_sleep", columnDefinition = "double precision default 8.0")
    @Builder.Default
    private Double goalSleep = 8.0;

    @Column(name = "goal_calories_burnt", columnDefinition = "integer default 500")
    @Builder.Default
    private Integer goalCaloriesBurnt = 500;

    @Column(name = "goal_calories_consumed", columnDefinition = "integer default 2000")
    @Builder.Default
    private Integer goalCaloriesConsumed = 2000;
}