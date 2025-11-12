package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.dtos.WorkoutRequestDTO;
import com.yourcompany.healthtracker.dtos.WorkoutResponseDTO;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.Workout;
import com.yourcompany.healthtracker.models.WorkoutLike;
import com.yourcompany.healthtracker.repositories.WorkoutLikeRepository;
import com.yourcompany.healthtracker.repositories.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final AuthenticationService authenticationService; // Để lấy user
    private final WorkoutLikeRepository workoutLikeRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    @Transactional
    public WorkoutResponseDTO logWorkout(WorkoutRequestDTO request) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();

        Workout workout = Workout.builder()
                .user(currentUser)
                .workoutType(request.getWorkoutType())
                .durationInMinutes(request.getDurationInMinutes())
                .caloriesBurned(request.getCaloriesBurned())
                .startedAt(request.getStartedAt())
                .distanceInKm(request.getDistanceInKm())
                .routePolyline(request.getRoutePolyline())
                .build();

        Workout savedWorkout = workoutRepository.save(workout);
        return WorkoutResponseDTO.fromEntity(savedWorkout, currentUser.getId());
    }

    public List<WorkoutResponseDTO> getMyWorkouts() {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        final Long currentUserId = currentUser.getId();

        return workoutRepository.findByUserOrderByStartedAtDesc(currentUser)
                .stream()
                .map(workout -> WorkoutResponseDTO.fromEntity(workout, currentUserId))
                .collect(Collectors.toList());
    }

    public List<WorkoutResponseDTO> getCommunityFeed() {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        final Long currentUserId = currentUser.getId();

        // Lấy 20 bài tập mới nhất
        return workoutRepository.findTop20ByOrderByStartedAtDesc()
                .stream()
                .map(workout -> WorkoutResponseDTO.fromEntity(workout, currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkoutResponseDTO toggleLike(Long workoutId) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài tập"));

        Optional<WorkoutLike> existingLike = workoutLikeRepository.findByUserAndWorkout(currentUser, workout);

        if (existingLike.isPresent()) {
            // Nếu đã like -> Bỏ like
            WorkoutLike like = existingLike.get();
            workout.getLikes().remove(like); // Xóa khỏi quan hệ
            workoutLikeRepository.delete(like);
        } else {
            // Nếu chưa like -> Like
            WorkoutLike newLike = WorkoutLike.builder()
                    .user(currentUser)
                    .workout(workout)
                    .build();
            workout.getLikes().add(newLike); // Thêm vào quan hệ

            User recipient = workout.getUser(); // Người chủ bài tập
            // Đảm bảo có người nhận, có token và không tự like
            if (recipient != null && recipient.getFcmToken() != null &&
                    !recipient.getId().equals(currentUser.getId())) {

                firebaseMessagingService.sendNotification(
                        recipient.getFcmToken(),
                        "Bài tập có lượt thích mới!", // Tiêu đề
                        currentUser.getFullName() + " đã thích bài tập " + workout.getWorkoutType().toString().toLowerCase() + " của bạn." // Nội dung
                );
            }
        }

        // Lưu lại workout để cập nhật (hoặc không cần nếu cascade hoạt động)
        // Cứ lưu lại cho chắc
        Workout updatedWorkout = workoutRepository.save(workout);

        // Trả về DTO đã cập nhật
        return WorkoutResponseDTO.fromEntity(updatedWorkout, currentUser.getId());
    }
}