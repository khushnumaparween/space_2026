package com.khushnuma.space2026.dto;

public record ApodApiResponse(
        String date,
        String title,
        String explanation,
        String url,
        String hdurl,
        String mediaType
) {
}
