package com.khushnuma.space2026.dto;

public record AsteroidApiResponse(
        String id,
        String name,
        Boolean potentiallyHazardous,
        Double diameterMinKm,
        Double diameterMaxKm,
        String closeApproachDate,
        String velocityKmPerHour,
        String missDistanceKm
) {
}