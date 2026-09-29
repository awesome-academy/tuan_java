package com.tuanhv.tripgoapi.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DepartureAvailabilityProjection {

    LocalDate getDate();

    Long getSlotsLeft();

    BigDecimal getPrice();
}
