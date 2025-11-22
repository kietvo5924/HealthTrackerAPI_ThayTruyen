package com.yourcompany.healthtracker.controllers;

import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.UserAchievement;
import com.yourcompany.healthtracker.services.AuthenticationService;
import com.yourcompany.healthtracker.services.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final GamificationService gamificationService;
    private final AuthenticationService authenticationService;

    @GetMapping("/my")
    public ResponseEntity<List<UserAchievement>> getMyAchievements() {
        User user = authenticationService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(gamificationService.getMyAchievements(user));
    }
}