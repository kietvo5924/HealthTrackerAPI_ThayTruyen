package com.yourcompany.healthtracker.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowStatsDTO {
    // Số người đang theo dõi mình
    private long followersCount;

    // Số người mình đang theo dõi
    private long followingCount;
}