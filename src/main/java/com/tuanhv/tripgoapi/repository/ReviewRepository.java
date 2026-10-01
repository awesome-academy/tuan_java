package com.tuanhv.tripgoapi.repository;

import com.tuanhv.tripgoapi.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByTour_Id(Long tourId);
}
