package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.dtos.ChangePasswordRequest;
import com.yourcompany.healthtracker.dtos.UpdateProfileRequest;
import com.yourcompany.healthtracker.dtos.UserResponseDTO;
import com.yourcompany.healthtracker.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Các API cho người dùng đã xác thực")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final AuthenticationService authenticationService;

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
}

