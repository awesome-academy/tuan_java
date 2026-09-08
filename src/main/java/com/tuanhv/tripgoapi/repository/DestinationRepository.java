package com.tuanhv.tripgoapi.repository;

import com.tuanhv.tripgoapi.dto.response.DestinationResponse;
import com.tuanhv.tripgoapi.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    @Query("""
        SELECT new com.tuanhv.tripgoapi.dto.response.DestinationResponse(
            d.id,
            d.slug,
            d.name,
            d.image,
            COUNT(t.id)
        )
        FROM Destination d
        LEFT JOIN Tour t
            ON t.destination = d
        GROUP BY d.id
    """)
    List<DestinationResponse> findAllWithTourCount();

}
