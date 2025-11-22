package com.yourcompany.healthtracker.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Schema(description = "Trạng thái kích hoạt tài khoản", example = "true")
    private boolean enabled;

    @Schema(description = "Trạng thái khóa tài khoản", example = "false")
    private boolean locked;

    @Schema(description = "Thời điểm tài khoản được tạo")
    private OffsetDateTime createdAt;

    @Schema(description = "Ngày sinh")
    private LocalDate dateOfBirth;

    @Schema(description = "Địa chỉ")
    private String address;

    @Schema(description = "Tiền sử bệnh án")
    private String medicalHistory;

    @Schema(description = "Dị ứng")
    private String allergies;

    @Schema(description = "Bật nhắc nhở uống nước")
    private boolean remindWater;

    @Schema(description = "Bật nhắc nhở đi ngủ")
    private boolean remindSleep;

    @Schema(description = "Mục tiêu bước đi")
    private Integer goalSteps;

    @Schema(description = "Mục tiêu nước (lít)")
    private Double goalWater;

    @Schema(description = "Mục tiêu ngủ (giờ)")
    private Double goalSleep;

    @Schema(description = "Mục tiêu calo vận động")
    private Integer goalCaloriesBurnt;

    @Schema(description = "Mục tiêu calo nạp vào")
    private Integer goalCaloriesConsumed;

    @Schema(description = "Số lượng người theo dõi")
    private long followersCount;

    @Schema(description = "Số lượng đang theo dõi")
    private long followingCount;

    @Schema(description = "Trạng thái: Người xem API này đã theo dõi user này chưa?", example = "true")
    @JsonProperty("isFollowing")
    private boolean isFollowing;

    // Cập nhật hàm này để nhận thêm tham số boolean isFollowing
    public static UserResponseDTO fromUser(User user, FollowStatsDTO stats, boolean isFollowing) {
        UserResponseDTOBuilder builder = UserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .locked(user.isLocked())
                .createdAt(user.getCreatedAt())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .medicalHistory(user.getMedicalHistory())
                .allergies(user.getAllergies())
                .remindWater(user.isRemindWater())
                .remindSleep(user.isRemindSleep());

        UserGoals goals = user.getUserGoals();
        builder.goalSteps(goals != null ? goals.getGoalSteps() : 10000);
        builder.goalWater(goals != null ? goals.getGoalWater() : 2.5);
        builder.goalSleep(goals != null ? goals.getGoalSleep() : 8.0);
        builder.goalCaloriesBurnt(goals != null ? goals.getGoalCaloriesBurnt() : 500);
        builder.goalCaloriesConsumed(goals != null ? goals.getGoalCaloriesConsumed() : 2000);

        // Map stats
        builder.followersCount(stats != null ? stats.getFollowersCount() : 0);
        builder.followingCount(stats != null ? stats.getFollowingCount() : 0);

        // Map isFollowing
        builder.isFollowing(isFollowing);

        return builder.build();
    }

    // Hàm overload để tương thích code cũ (mặc định false)
    public static UserResponseDTO fromUser(User user, FollowStatsDTO stats) {
        return fromUser(user, stats, false);
    }

    // Hàm overload cơ bản nhất
    public static UserResponseDTO fromUser(User user) {
        return fromUser(user, null, false);
    }
}