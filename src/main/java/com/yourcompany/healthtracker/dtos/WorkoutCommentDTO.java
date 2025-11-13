package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.WorkoutComment;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class WorkoutCommentDTO {
    private Long id;
    private String text;
    private Instant createdAt;
    private String userFullName;
    private Long userId;

    public static WorkoutCommentDTO fromEntity(WorkoutComment comment) {
        return WorkoutCommentDTO.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .userFullName(comment.getUser().getFullName())
                .userId(comment.getUser().getId())
                .build();
    }
}