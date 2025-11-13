package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.dtos.AddCommentRequestDTO;
import com.yourcompany.healthtracker.dtos.WorkoutCommentDTO;
import com.yourcompany.healthtracker.dtos.WorkoutRequestDTO;
import com.yourcompany.healthtracker.dtos.WorkoutResponseDTO;
import com.yourcompany.healthtracker.services.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "Thích hoặc bỏ thích một bài tập",
            description = "Toggle trạng thái 'like' của user hiện tại trên một bài tập.")
    @PostMapping("/{id}/like")
    public ResponseEntity<WorkoutResponseDTO> toggleLike(@PathVariable Long id) {
        WorkoutResponseDTO updatedWorkout = workoutService.toggleLike(id);
        return ResponseEntity.ok(updatedWorkout);
    }

    @Operation(summary = "Lấy danh sách bình luận của bài tập",
            description = "Lấy tất cả bình luận cho một bài tập theo ID")
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<WorkoutCommentDTO>> getComments(@PathVariable Long id) {
        List<WorkoutCommentDTO> comments = workoutService.getCommentsForWorkout(id);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Thêm bình luận mới vào bài tập",
            description = "Thêm một bình luận (dưới dạng text) vào bài tập")
    @PostMapping("/{id}/comments")
    public ResponseEntity<WorkoutCommentDTO> addComment(
            @PathVariable Long id,
            @RequestBody AddCommentRequestDTO request
    ) {
        // (Trong thực tế, bạn nên tạo một DTO Request
        // thay vì dùng String, nhưng dùng String cho nhanh)
        WorkoutCommentDTO newComment = workoutService.addCommentToWorkout(id, request.getText());
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }
}