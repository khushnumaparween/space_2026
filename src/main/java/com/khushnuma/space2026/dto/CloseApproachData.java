package com.khushnuma.space2026.dto;

public record CloseApproachData(
        String close_approach_date,
        String close_approach_date_full,
        RelativeVelocity relative_velocity,
        MissDistance miss_distance,
        String orbiting_body
) {
}
