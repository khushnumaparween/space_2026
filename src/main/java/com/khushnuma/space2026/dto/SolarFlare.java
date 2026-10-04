package com.khushnuma.space2026.dto;

public record SolarFlare(
        String flrID,
        String beginTime,
        String peakTime,
        String endTime,
        String classType,
        String sourceLocation
) {
}
