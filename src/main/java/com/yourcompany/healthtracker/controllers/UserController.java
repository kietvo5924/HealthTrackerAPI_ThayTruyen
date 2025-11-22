package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.dtos.*;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.repositories.UserFollowRepository;
import com.yourcompany.healthtracker.repositories.UserRepository;
import com.yourcompany.healthtracker.services.AuthenticationService;
import com.yourcompany.healthtracker.services.UserFollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Các API cho người dùng đã xác thực")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserFollowService userFollowService;
    private final UserFollowRepository userFollowRepository;

    @Operation(summary = "Lấy thông tin cá nhân", description = "Lấy thông tin chi tiết của người dùng đang đăng nhập.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập (chưa đăng nhập)")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile() {
        UserResponseDTO userProfile = authenticationService.getMyProfile();
        return ResponseEntity.ok(userProfile);
    }

    @Operation(summary = "Cập nhật thông tin cá nhân", description = "Cập nhật thông tin chi tiết của người dùng đang đăng nhập.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công, trả về thông tin mới"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserResponseDTO updatedUser = authenticationService.updateMyProfile(request);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Đổi mật khẩu", description = "Người dùng tự đổi mật khẩu của chính mình.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc mật khẩu hiện tại không đúng"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    @PutMapping("/me/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        try {
            authenticationService.changePassword(request);
            return ResponseEntity.ok("Đổi mật khẩu thành công.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Lưu/Cập nhật FCM Token",
            description = "Gửi FCM token của thiết bị lên server để nhận thông báo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lưu token thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    @PostMapping("/me/fcm-token")
    public ResponseEntity<String> saveFcmToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token không được rỗng.");
        }

        try {
            authenticationService.saveFcmToken(token);
            return ResponseEntity.ok("Token đã được lưu.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    @Operation(summary = "Cập nhật cài đặt thông báo",
            description = "Bật/tắt các nhắc nhở cho người dùng đang đăng nhập.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    @PutMapping("/me/notification-settings")
    public ResponseEntity<UserResponseDTO> updateNotificationSettings(
            @Valid @RequestBody NotificationSettingsDTO settingsDTO) {

        UserResponseDTO updatedUser = authenticationService.updateNotificationSettings(settingsDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Cập nhật mục tiêu cá nhân",
            description = "Cập nhật các mục tiêu sức khỏe (bước, nước, ngủ, calo...)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "403", description = "Không có quyền truy cập")
    })
    @PutMapping("/me/goals")
    public ResponseEntity<UserResponseDTO> updateUserGoals(
            @Valid @RequestBody UserGoalsDTO goalsDTO) {

        UserResponseDTO updatedUser = authenticationService.updateUserGoals(goalsDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        List<User> users = userRepository.findByFullNameContainingIgnoreCase(query);

        List<UserResponseDTO> dtos = users.stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .map(user -> {
                    FollowStatsDTO stats = userFollowService.getFollowStats(user.getId());

                    // 2. QUAN TRỌNG: Kiểm tra quan hệ follow trong Database
                    boolean isFollowing = userFollowRepository.existsByFollowerAndFollowing(currentUser, user);

                    // 3. Truyền kết quả (true/false) vào DTO
                    return UserResponseDTO.fromUser(user, stats, isFollowing);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}

