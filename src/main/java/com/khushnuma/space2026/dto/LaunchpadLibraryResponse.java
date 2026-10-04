package com.khushnuma.space2026.dto;

import java.util.List;

public record LaunchpadLibraryResponse(
        int count,
        String next,
        String previous,
        List<Launchpad> results
) {

    public record Launchpad(
            String id,
            String name,
            Location location,
            String latitude,
            String longitude,
            String map_url,
            String status,
            Integer total_launch_count,
            Integer successful_launches
    ) {
    }

    public record Location(
            String name
    ) {
    }
}