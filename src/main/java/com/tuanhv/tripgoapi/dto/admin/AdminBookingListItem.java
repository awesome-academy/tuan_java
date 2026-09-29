package com.tuanhv.tripgoapi.dto.admin;

import com.tuanhv.tripgoapi.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record AdminBookingListItem(
        Long id,
        String code,
        String customerName,
        String customerEmail,
        String tourTitle,
        String tourSlug,
        LocalDate departureDate,
        int adults,
        int children,
        int totalGuests,
        BigDecimal totalPrice,
        BookingStatus status,
        Instant createdAt
) {
}
