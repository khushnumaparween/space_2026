package com.khushnuma.space2026.dto;

public record OrbitalData(
        String orbit_id,
        String orbit_determination_date,
        String first_observation_date,
        String last_observation_date,
        String data_arc_in_days,
        String observations_used
) {
}
