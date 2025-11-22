package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.dtos.*;
import com.yourcompany.healthtracker.models.*;
import com.yourcompany.healthtracker.repositories.UserFollowRepository;
import com.yourcompany.healthtracker.repositories.WorkoutCommentRepository;
import com.yourcompany.healthtracker.repositories.WorkoutLikeRepository;
import com.yourcompany.healthtracker.repositories.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final AuthenticationService authenticationService; // Để lấy user
    private final WorkoutLikeRepository workoutLikeRepository;
    private final FirebaseMessagingService firebaseMessagingService;
    private final WorkoutCommentRepository workoutCommentRepository;
    private final HealthDataService healthDataService;
    private final UserFollowRepository userFollowRepository;
    private final GamificationService gamificationService;

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

        // Tự động cộng dồn calo vào HealthData
        if (savedWorkout.getCaloriesBurned() != null && savedWorkout.getCaloriesBurned() > 0) {
            try {
                // 1. Lấy ngày của bài tập
                LocalDate workoutDate = savedWorkout.getStartedAt().toLocalDate();

                // 2. Tạo request cho HealthData
                HealthDataLogRequest healthRequest = new HealthDataLogRequest();
                healthRequest.setDate(workoutDate);
                healthRequest.setCaloriesBurnt(savedWorkout.getCaloriesBurned()); // Chỉ gửi calo

                // 3. Gọi service để cộng dồn
                healthDataService.logOrUpdateHealthData(healthRequest);

                log.info("Đã tự động cộng dồn {} calo vào HealthData ngày {}",
                        savedWorkout.getCaloriesBurned(), workoutDate);

            } catch (Exception e) {
                // Nếu có lỗi (ví dụ: ngày tương lai), chỉ ghi log, không làm hỏng cả giao dịch
                log.error("Không thể cộng dồn calo từ workout: {}", e.getMessage());
            }
        }

        // Kiểm tra Thành tựu (Gamification)
        try {
            // A. Luôn kiểm tra số lượng bài tập
            gamificationService.checkWorkoutCountAchievements(currentUser);

            // B. Kiểm tra thành tựu Calo (SỬA LỖI Ở ĐÂY)
            if (savedWorkout.getCaloriesBurned() != null && savedWorkout.getCaloriesBurned() > 0) {
                // Thêm .intValue() để chuyển Double -> int
                gamificationService.checkCalorieAchievements(currentUser, savedWorkout.getCaloriesBurned().intValue());
            }
        } catch (Exception e) {
            log.error("Lỗi khi kiểm tra thành tựu: {}", e.getMessage());
        }

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

    public List<WorkoutResponseDTO> getCommunityFeed(int page, int size) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        final Long currentUserId = currentUser.getId();

        Pageable pageable = PageRequest.of(page, size);
        Page<Workout> workoutPage;

        // 1. Lấy danh sách những người mình đang follow
        List<Long> followingIds = userFollowRepository.findAllFollowingIds(currentUser);

        if (followingIds.isEmpty()) {
            // 2a. CASE 1: Nếu chưa follow ai -> Hiện Global Feed (Tất cả mọi người)
            // Để người dùng mới không thấy màn hình trống trơn
            workoutPage = workoutRepository.findAllByOrderByStartedAtDesc(pageable);
        } else {
            // 2b. CASE 2: Nếu đã follow -> Chỉ hiện bài của mình và người mình follow
            followingIds.add(currentUserId); // Thêm bài của chính mình vào
            workoutPage = workoutRepository.findByUserIdInOrderByStartedAtDesc(followingIds, pageable);
        }

        return workoutPage.getContent()
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
            if (recipient != null && !recipient.getId().equals(currentUser.getId())) {
                firebaseMessagingService.sendNotificationToUser(
                        recipient,
                        "Bài tập có lượt thích mới!",
                        currentUser.getFullName() + " đã thích bài tập của bạn.",
                        Notification.NotificationType.SOCIAL
                );
            }
        }

        Workout updatedWorkout = workoutRepository.save(workout);

        // Trả về DTO đã cập nhật
        return WorkoutResponseDTO.fromEntity(updatedWorkout, currentUser.getId());
    }

    public List<WorkoutCommentDTO> getCommentsForWorkout(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài tập"));

        return workoutCommentRepository.findByWorkoutOrderByCreatedAtAsc(workout)
                .stream()
                .map(WorkoutCommentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkoutCommentDTO addCommentToWorkout(Long workoutId, String text) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài tập"));

        WorkoutComment comment = WorkoutComment.builder()
                .text(text)
                .user(currentUser)
                .workout(workout)
                .build();

        // Thêm comment vào list của workout
        workout.getComments().add(comment);
        // Lưu comment (hoặc lưu workout, tùy vào cascade)
        WorkoutComment savedComment = workoutCommentRepository.save(comment);

        // (TÙY CHỌN: Gửi Noti cho chủ bài viết)
        User recipient = workout.getUser();
        if (recipient != null && !recipient.getId().equals(currentUser.getId())) {

            firebaseMessagingService.sendNotificationToUser(
                    recipient,
                    "Bài tập có bình luận mới!",
                    currentUser.getFullName() + " đã bình luận bài tập của bạn: ",
                    Notification.NotificationType.SOCIAL
            );
        }

        return WorkoutCommentDTO.fromEntity(savedComment);
    }

    @Transactional(readOnly = true)
    public List<WorkoutSummaryDTO> getWorkoutSummary(LocalDate startDate, LocalDate endDate) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        return workoutRepository.findWorkoutSummaryByDateRange(currentUser, startDate, endDate);
    }
}