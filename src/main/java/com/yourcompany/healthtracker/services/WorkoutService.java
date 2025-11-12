package com.yourcompany.healthtracker.services;

import com.yourcompany.healthtracker.dtos.WorkoutRequestDTO;
import com.yourcompany.healthtracker.dtos.WorkoutResponseDTO;
import com.yourcompany.healthtracker.models.User;
import com.yourcompany.healthtracker.models.Workout;
import com.yourcompany.healthtracker.repositories.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final AuthenticationService authenticationService; // Để lấy user

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
        return WorkoutResponseDTO.fromEntity(savedWorkout);
    }

    public List<WorkoutResponseDTO> getMyWorkouts() {
        User currentUser = authenticationService.getCurrentAuthenticatedUser();

        return workoutRepository.findByUserOrderByStartedAtDesc(currentUser)
                .stream()
                .map(WorkoutResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // (Bạn có thể thêm hàm deleteWorkout(Long workoutId) ở đây)
}