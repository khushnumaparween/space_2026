package com.khushnuma.space2026.dto;

import java.util.List;

public record RocketLibraryResponse(
        int count,
        String next,
        String previous,
        List<Rocket> results
) {

    public record Rocket(
            String id,
            String name,
            Configuration configuration
    ) {

        public record Configuration(
                String name,
                String full_name,
                String variant,
                String manufacturer,
                String description
        ) {
        }
    }
}
