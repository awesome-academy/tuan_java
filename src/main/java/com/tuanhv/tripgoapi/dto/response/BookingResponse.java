package com.tuanhv.tripgoapi.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record BookingResponse(
        Long id,
        String code,
        String status,

        Long departureId,
        String tourSlug,
        String tourTitle,
        LocalDate date,

        Integer adults,
        Integer children,
        BigDecimal totalPrice,

        ContactResponse contact,

        Instant createdAt
) {
}
