package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.Notification;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class NotificationResponseDTO {
    private Long id;
    private String title;
    private String body;
    private String type;
    private boolean isRead;
    private OffsetDateTime createdAt;

    public static NotificationResponseDTO fromEntity(Notification n) {
        return NotificationResponseDTO.builder()
                .id(n.getId())
                .title(n.getTitle())
                .body(n.getBody())
                .type(n.getType().name())
                .isRead(n.isRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}