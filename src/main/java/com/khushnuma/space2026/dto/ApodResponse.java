package com.khushnuma.space2026.dto;

public record ApodResponse(
        String date,
        String title,
        String explanation,
        String url,
        String hdurl,
        String media_type
) {
}