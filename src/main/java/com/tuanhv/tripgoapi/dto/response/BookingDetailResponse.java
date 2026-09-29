package com.tuanhv.tripgoapi.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record BookingDetailResponse(
        Long id,
        String code,
        TourSummaryResponse tour,
        Long departureId,
        LocalDate date,
        Integer adults,
        Integer children,
        BigDecimal totalPrice,
        String status,
        ContactResponse contact,
        Instant createdAt
) {
}
