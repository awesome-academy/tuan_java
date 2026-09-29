package com.tuanhv.tripgoapi.dto.response;

import java.time.LocalDate;

public record TourDepartureResponse(
        Long id,
        LocalDate startDate
) {
}
