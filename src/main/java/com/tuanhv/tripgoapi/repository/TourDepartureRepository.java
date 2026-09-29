package com.tuanhv.tripgoapi.repository;

import com.tuanhv.tripgoapi.entity.TourDeparture;
import com.tuanhv.tripgoapi.repository.projection.DepartureAvailabilityProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TourDepartureRepository extends JpaRepository<TourDeparture, Long> {

    @Query("""
        SELECT td
        FROM TourDeparture td
        WHERE td.tour.id = :tourId
          AND td.startDate >= CURRENT_DATE
        ORDER BY td.startDate
    """)
    List<TourDeparture> findUpcomingByTourId(@Param("tourId") Long tourId);

    @Query("""
        SELECT
            td.startDate AS date,

            t.maxGroupSize -
            COALESCE(
                SUM(
                    CASE
                        WHEN b.status IN (
                            com.tuanhv.tripgoapi.enums.BookingStatus.PENDING,
                            com.tuanhv.tripgoapi.enums.BookingStatus.CONFIRMED
                        )
                        THEN b.adults + b.children
                        ELSE 0
                    END
                ),
                0
            ) AS slotsLeft,

            COALESCE(td.price, t.discountPrice, t.price) AS price

        FROM TourDeparture td

        JOIN td.tour t

        LEFT JOIN Booking b
            ON b.tourDeparture = td

        WHERE t.slug = :slug
          AND td.startDate >= CURRENT_DATE
          AND td.startDate >= :fromDate
          AND td.startDate < :toDate

        GROUP BY
            td.id,
            td.startDate,
            t.maxGroupSize,
            td.price,
            t.discountPrice,
            t.price

        ORDER BY td.startDate ASC
    """)
    List<DepartureAvailabilityProjection> findAvailability(
            @Param("slug") String slug,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT td
        FROM TourDeparture td
        WHERE td.id = :departureId
    """)
    Optional<TourDeparture> findByIdForUpdate(@Param("departureId") Long departureId);
}
