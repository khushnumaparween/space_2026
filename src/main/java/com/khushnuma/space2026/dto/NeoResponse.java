package com.khushnuma.space2026.dto;

import java.util.List;
import java.util.Map;

public record NeoResponse(
        Map<String, List<Asteroid>> near_earth_objects
) {
}