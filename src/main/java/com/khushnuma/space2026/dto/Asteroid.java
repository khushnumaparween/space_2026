package com.khushnuma.space2026.dto;

import java.util.List;

public record Asteroid(
        String id,
        String name,
        Boolean potentially_hazardous_asteroid,
        Double absolute_magnitude_h,
        EstimatedDiameter estimated_diameter,
        List<CloseApproachData> close_approach_data,
        OrbitalData orbital_data
) {
}