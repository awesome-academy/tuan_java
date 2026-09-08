package com.tuanhv.tripgoapi.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record TourDetailResponse(
        Long id,
        String slug,
        String title,

        String destination,
        String category,

        Integer durationDays,
        BigDecimal price,
        BigDecimal discountPrice,

        BigDecimal rating,
        Integer reviewCount,

        String description,
        Integer maxGroupSize,
        String thumbnail,

        List<String> images,
        List<TourItineraryResponse> itineraries,
        List<TourDepartureResponse> departures
) {
}
