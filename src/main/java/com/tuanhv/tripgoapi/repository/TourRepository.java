package com.tuanhv.tripgoapi.repository;

import com.tuanhv.tripgoapi.entity.Tour;
import com.tuanhv.tripgoapi.entity.TourItinerary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {

    boolean existsBySlug(String slug);

    @Override
    @EntityGraph(attributePaths = {
            "destination",
            "category"
    })
    Page<Tour> findAll(Specification<Tour> spec, Pageable pageable);

    @Query("""
        SELECT t
        FROM Tour t
        JOIN FETCH t.destination
        JOIN FETCH t.category
        WHERE t.slug = :slug
    """)
    Optional<Tour> findDetailBySlug(@Param("slug") String slug);

    @Query("""
        SELECT i.fileName
        FROM Tour t
        JOIN t.images i
        WHERE t.id = :tourId
    """)
    List<String> findImagesByTourId(@Param("tourId") Long tourId);

    @Query("""
        SELECT i
        FROM Tour t
        JOIN t.itineraries i
        WHERE t.id = :tourId
        ORDER BY i.dayNumber
    """)
    List<TourItinerary> findItineraryByTourId(@Param("tourId") Long tourId);


}
