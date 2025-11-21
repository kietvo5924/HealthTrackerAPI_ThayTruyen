package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.dtos.FollowStatsDTO;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.UserFollow;
import com.yourcompany.healthtracker.repositories.UserFollowRepository;
import com.yourcompany.healthtracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final FirebaseMessagingService firebaseMessagingService; // Đã inject thêm service này

    @Transactional
    public void followUser(Long targetUserId) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();

        // 1. Không được tự follow chính mình
        if (currentUser.getId().equals(targetUserId)) {
            throw new RuntimeException("Bạn không thể tự theo dõi chính mình");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // 2. Nếu chưa follow thì mới tạo
        if (!userFollowRepository.existsByFollowerAndFollowing(currentUser, targetUser)) {
            UserFollow follow = UserFollow.builder()
                    .follower(currentUser)
                    .following(targetUser)
                    .build();
            userFollowRepository.save(follow);

            // 3. Gửi thông báo (Logic đã được thêm vào đây)
            String fcmToken = targetUser.getFcmToken();
            if (fcmToken != null && !fcmToken.isEmpty()) {
                String title = "Bạn có người theo dõi mới!";
                String body = currentUser.getFullName() + " đã bắt đầu theo dõi bạn.";

                // Gọi FirebaseMessagingService để gửi
                firebaseMessagingService.sendNotification(fcmToken, title, body);
                log.info("Đã gửi thông báo follow đến user ID: {}", targetUserId);
            }
        }
    }

    @Transactional
    public void unfollowUser(Long targetUserId) {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        // Tìm và xóa relationship
        userFollowRepository.findByFollowerAndFollowing(currentUser, targetUser)
                .ifPresent(userFollowRepository::delete);
    }

    // Lấy thống kê (số followers, số following)
    public FollowStatsDTO getFollowStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return FollowStatsDTO.builder()
                .followersCount(userFollowRepository.countByFollowing(user))
                .followingCount(userFollowRepository.countByFollower(user))
                .build();
    }
}