package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.services.UserFollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/social")
@RequiredArgsConstructor
public class UserFollowController {

    private final UserFollowService userFollowService;

    @PostMapping("/follow/{userId}")
    public ResponseEntity<String> followUser(@PathVariable Long userId) {
        userFollowService.followUser(userId);
        return ResponseEntity.ok("Đã theo dõi thành công");
    }

    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId) {
        userFollowService.unfollowUser(userId);
        return ResponseEntity.ok("Đã hủy theo dõi");
    }
}