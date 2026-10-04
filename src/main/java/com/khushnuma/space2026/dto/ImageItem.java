package com.khushnuma.space2026.dto;

import java.util.List;

public record ImageItem<ImageData, ImageLink>(
        List<ImageData> data,
        List<ImageLink> links
) {
}