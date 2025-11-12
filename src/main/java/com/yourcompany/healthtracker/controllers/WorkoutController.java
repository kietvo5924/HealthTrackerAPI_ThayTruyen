package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.dtos.WorkoutRequestDTO;
import com.yourcompany.healthtracker.dtos.WorkoutResponseDTO;
import com.yourcompany.healthtracker.services.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@Tag(name = "Workout API", description = "APIs để ghi và đọc lịch sử bài tập")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu xác thực
public class WorkoutController {

    private final WorkoutService workoutService;

    @Operation(summary = "Ghi (log) một bài tập mới")
    @PostMapping
    public ResponseEntity<WorkoutResponseDTO> logWorkout(
            @Valid @RequestBody WorkoutRequestDTO request
    ) {
        WorkoutResponseDTO response = workoutService.logWorkout(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy lịch sử bài tập của tôi",
            description = "Lấy danh sách tất cả bài tập của người dùng đang đăng nhập.")
    @GetMapping("/me")
    public ResponseEntity<List<WorkoutResponseDTO>> getMyWorkouts() {
        List<WorkoutResponseDTO> workouts = workoutService.getMyWorkouts();
        return ResponseEntity.ok(workouts);
    }

    @Operation(summary = "Lấy bảng tin (feed) cộng đồng",
            description = "Lấy danh sách 20 bài tập mới nhất từ tất cả người dùng.")
    @GetMapping("/feed")
    public ResponseEntity<List<WorkoutResponseDTO>> getCommunityFeed() {
        List<WorkoutResponseDTO> feed = workoutService.getCommunityFeed();
        return ResponseEntity.ok(feed);
    }
}