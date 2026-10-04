package com.khushnuma.space2026.dto;

import java.util.List;

public record CollectionData<ImageItem>(
        String version,
        String href,
        List<ImageItem> items
) {
}