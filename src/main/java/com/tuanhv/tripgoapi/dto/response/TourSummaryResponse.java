package com.tuanhv.tripgoapi.dto.response;

public record TourSummaryResponse(
        Long id,
        String slug,
        String title,
        String thumbnail
) {
}
