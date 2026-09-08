package com.tuanhv.tripgoapi.specification;

import com.tuanhv.tripgoapi.dto.request.TourSearchRequest;
import com.tuanhv.tripgoapi.entity.Tour;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class TourSpecification {

    private TourSpecification() {}

    public static Specification<Tour> search(TourSearchRequest request) {
        return Specification
                .where(keyword(request.getQ()))
                .and(isFeatured(request.getFeatured()))
                .and(destination(request.getDestination()))
                .and(category(request.getCategory()))
                .and(minPrice(request.getMinPrice()))
                .and(maxPrice(request.getMaxPrice()))
                .and(duration(request.getDuration()))
                .and(minRating(request.getRating()));
    }

    private static Specification<Tour> isFeatured(Boolean isFeatured) {
        return (root, query, cb) -> {

            if (isFeatured == null) {
                return null;
            }

            return cb.equal(
                    root.get("isFeatured"),
                    isFeatured
            );
        };
    }

    private static Specification<Tour> keyword(String q) {
        return (root, query, cb) -> {

            if (q == null || q.isBlank()) {
                return null;
            }

            String keyword = "%" + q.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(
                            cb.lower(root.get("title")),
                            keyword
                    ),
                    cb.like(
                            cb.lower(root.get("description")),
                            keyword
                    )
            );
        };
    }

    private static Specification<Tour> destination(String slug) {
        return (root, query, cb) -> {

            if (slug == null || slug.isBlank()) {
                return null;
            }

            return cb.equal(
                    root.get("destination").get("slug"),
                    slug
            );
        };
    }

    private static Specification<Tour> category(String slug) {
        return (root, query, cb) -> {

            if (slug == null || slug.isBlank()) {
                return null;
            }

            return cb.equal(
                    root.get("category").get("slug"),
                    slug
            );
        };
    }

    private static Specification<Tour> minPrice(BigDecimal minPrice) {
        return (root, query, cb) -> {

            if (minPrice == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(
                    root.get("price"),
                    minPrice
            );
        };
    }

    private static Specification<Tour> maxPrice(BigDecimal maxPrice) {
        return (root, query, cb) -> {

            if (maxPrice == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(
                    root.get("price"),
                    maxPrice
            );
        };
    }

    private static Specification<Tour> duration(Integer duration) {
        return (root, query, cb) -> {

            if (duration == null) {
                return null;
            }

            return cb.equal(
                    root.get("durationDays"),
                    duration
            );
        };
    }

    private static Specification<Tour> minRating(BigDecimal rating) {
        return (root, query, cb) -> {

            if (rating == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(
                    root.get("rating"),
                    rating
            );
        };
    }
}
