package com.tuanhv.tripgoapi.dto.admin;

import java.math.BigDecimal;

public record AdminDashboardView(
        long totalTours,
        long totalBookings,
        long pendingBookings,
        long confirmedBookings,
        BigDecimal confirmedRevenue
) {
}
