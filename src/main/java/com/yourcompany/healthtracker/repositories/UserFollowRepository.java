package com.yourcompany.healthtracker.repositories;

import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    // Kiểm tra xem A có đang follow B không
    boolean existsByFollowerAndFollowing(User follower, User following);

    // Tìm mối quan hệ để xóa (Unfollow)
    Optional<UserFollow> findByFollowerAndFollowing(User follower, User following);

    // Đếm số người mình đang follow
    long countByFollower(User user);

    // Đếm số người đang follow mình (Followers)
    long countByFollowing(User user);

    // Lấy danh sách ID của những người mình đang follow (Để lọc Feed)
    @Query("SELECT f.following.id FROM UserFollow f WHERE f.follower = :follower")
    List<Long> findAllFollowingIds(User follower);
}