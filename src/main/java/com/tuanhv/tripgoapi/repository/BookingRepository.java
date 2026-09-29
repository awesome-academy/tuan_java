package com.tuanhv.tripgoapi.repository;

import com.tuanhv.tripgoapi.entity.Booking;
import com.tuanhv.tripgoapi.enums.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    long countByStatus(BookingStatus status);

    @Query("""
        SELECT COALESCE(
            SUM(b.totalPrice),
            0
        )
        FROM Booking b
        WHERE b.status = com.tuanhv.tripgoapi.enums.BookingStatus.CONFIRMED
    """)
    BigDecimal sumConfirmedRevenue();

    @Query("""
        SELECT CASE
            WHEN COUNT(b) > 0 THEN true
            ELSE false
        END
        FROM Booking b
        WHERE b.tourDeparture.tour.id = :tourId
    """)
    boolean existsByTourId(@Param("tourId") Long tourId);

    @EntityGraph(attributePaths = {
            "user",
            "tourDeparture",
            "tourDeparture.tour"
    })
    Page<Booking> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {
            "user",
            "tourDeparture",
            "tourDeparture.tour"
    })
    Page<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b
        FROM Booking b
        WHERE b.id = :id
    """)
    Optional<Booking> findByIdForAdminUpdate(@Param("id") Long id);

    @Query("""
        SELECT COALESCE(
            SUM(b.adults + b.children),
            0
        )
        FROM Booking b
        WHERE b.tourDeparture.id = :departureId
          AND b.status IN (
              com.tuanhv.tripgoapi.enums.BookingStatus.PENDING,
              com.tuanhv.tripgoapi.enums.BookingStatus.CONFIRMED
          )
    """)
    Long sumBookedSeats(@Param("departureId") Long departureId);

    @EntityGraph(attributePaths = {
            "tourDeparture",
            "tourDeparture.tour"
    })
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {
            "tourDeparture",
            "tourDeparture.tour"
    })
    List<Booking> findByUserIdAndStatusOrderByCreatedAtDesc(
            Long userId,
            BookingStatus status
    );

    @EntityGraph(attributePaths = {
            "tourDeparture",
            "tourDeparture.tour"
    })
    Optional<Booking> findByIdAndUserId(
            Long id,
            Long userId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b
        FROM Booking b
        JOIN FETCH b.user
        JOIN FETCH b.tourDeparture td
        JOIN FETCH td.tour
        WHERE b.id = :id
    """)
    Optional<Booking> findByIdForUpdate(@Param("id") Long id);
}
