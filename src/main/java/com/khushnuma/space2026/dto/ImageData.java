package com.khushnuma.space2026.dto;

import java.util.List;

public record ImageData(
        String nasa_id,
        String title,
        String description,
        String media_type,
        String date_created,
        String center,
        List<String> keywords
) {
}
