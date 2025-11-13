package com.yourcompany.healthtracker.dtos;

import com.yourcompany.healthtracker.models.Role;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.UserGoals;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Builder
@Schema(description = "Đối tượng trả về chứa thông tin công khai của người dùng")
public class UserResponseDTO {

    @Schema(description = "ID duy nhất của người dùng", example = "1")
    private Long id;

    @Schema(description = "Họ và tên đầy đủ", example = "Nguyễn Văn A")
    private String fullName;

    @Schema(description = "Địa chỉ email", example = "nguyenvana@example.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0987654321")
    private String phoneNumber;

    @Schema(description = "Vai trò của người dùng trong hệ thống", example = "PATIENT")
    private Role role;

    @Schema(description = "Trạng thái kích hoạt tài khoản (true = đã kích hoạt)", example = "true")
    private boolean enabled;

    @Schema(description = "Trạng thái khóa tài khoản (true = đã bị khóa)", example = "false")
    private boolean locked;

    @Schema(description = "Thời điểm tài khoản được tạo", example = "2025-09-10T10:30:00+07:00")
    private OffsetDateTime createdAt;

    @Schema(description = "Ngày sinh", example = "2000-01-15")
    private LocalDate dateOfBirth;

    @Schema(description = "Địa chỉ", example = "123 Đường ABC, Quận 1, TP.HCM")
    private String address;

    @Schema(description = "Tiền sử bệnh án", example = "Từng bị viêm phổi lúc nhỏ")
    private String medicalHistory;

    @Schema(description = "Dị ứng", example = "Dị ứng với phấn hoa")
    private String allergies;

    @Schema(description = "Bật nhắc nhở uống nước", example = "true")
    private boolean remindWater;

    @Schema(description = "Bật nhắc nhở đi ngủ", example = "true")
    private boolean remindSleep;

    @Schema(description = "Mục tiêu bước đi", example = "10000")
    private Integer goalSteps;

    @Schema(description = "Mục tiêu nước (lít)", example = "2.5")
    private Double goalWater;

    @Schema(description = "Mục tiêu ngủ (giờ)", example = "8.0")
    private Double goalSleep;

    @Schema(description = "Mục tiêu calo vận động", example = "500")
    private Integer goalCaloriesBurnt;

    @Schema(description = "Mục tiêu calo nạp vào", example = "2000")
    private Integer goalCaloriesConsumed;

    public static UserResponseDTO fromUser(User user) {
        UserResponseDTOBuilder builder = UserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .locked(user.isLocked())
                .createdAt(user.getCreatedAt());

        builder.dateOfBirth(user.getDateOfBirth());
        builder.address(user.getAddress());
        builder.medicalHistory(user.getMedicalHistory());
        builder.allergies(user.getAllergies());
        builder.remindWater(user.isRemindWater());
        builder.remindSleep(user.isRemindSleep());

        UserGoals goals = user.getUserGoals();

        builder.goalSteps(goals != null ? goals.getGoalSteps() : 10000);
        builder.goalWater(goals != null ? goals.getGoalWater() : 2.5);
        builder.goalSleep(goals != null ? goals.getGoalSleep() : 8.0);
        builder.goalCaloriesBurnt(goals != null ? goals.getGoalCaloriesBurnt() : 500);
        builder.goalCaloriesConsumed(goals != null ? goals.getGoalCaloriesConsumed() : 2000);

        return builder.build();
    }
}
