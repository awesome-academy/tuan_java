package com.tuanhv.tripgoapi.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record BookingSummaryResponse(
        Long id,
        String code,
        TourSummaryResponse tour,
        LocalDate date,
        Integer adults,
        Integer children,
        BigDecimal totalPrice,
        String status,
        Instant createdAt
) {
}
