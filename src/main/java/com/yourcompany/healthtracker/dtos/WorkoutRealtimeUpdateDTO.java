package com.yourcompany.healthtracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutRealtimeUpdateDTO {
    private Long workoutId;
    private long likeCount;
    private long commentCount;
    private String eventType;
    private Long actorId;
    private String actorName;
}
