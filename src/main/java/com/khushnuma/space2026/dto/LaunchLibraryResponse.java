package com.khushnuma.space2026.dto;

import java.util.List;

public record LaunchLibraryResponse(
        int count,
        String next,
        String previous,
        List<SpaceXLaunchResponse> results
) {
}