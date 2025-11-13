package com.yourcompany.healthtracker.dtos;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Đối tượng để cập nhật cài đặt thông báo")
public class NotificationSettingsDTO {

    @Schema(description = "Bật/tắt nhắc nhở uống nước", example = "true")
    private boolean remindWater;

    @Schema(description = "Bật/tắt nhắc nhở đi ngủ", example = "true")
    private boolean remindSleep;
}