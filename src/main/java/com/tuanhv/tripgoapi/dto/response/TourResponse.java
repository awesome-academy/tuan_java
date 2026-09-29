package com.tuanhv.tripgoapi.dto.response;

import java.math.BigDecimal;

public record TourResponse(
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
        String thumbnail
) {
}
