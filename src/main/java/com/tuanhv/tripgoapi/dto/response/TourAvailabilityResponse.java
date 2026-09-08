package com.tuanhv.tripgoapi.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TourAvailabilityResponse(
        LocalDate date,
        Integer slotsLeft,
        BigDecimal price
) {
}
