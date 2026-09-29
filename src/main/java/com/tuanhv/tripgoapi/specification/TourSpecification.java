package com.tuanhv.tripgoapi.specification;

import com.tuanhv.tripgoapi.dto.request.TourSearchRequest;
import com.tuanhv.tripgoapi.entity.Tour;
import com.tuanhv.tripgoapi.exception.BadRequestException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
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
                .and(minRating(request.getRating()))
                .and(sort(request.getSort()));
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

            String escaped = escapeLike(q.trim().toLowerCase());
            String pattern = "%" + escaped + "%";

            return cb.or(
                    cb.like(
                            cb.lower(root.get("title")),
                            pattern,
                            '\\'
                    ),
                    cb.like(
                            cb.lower(root.get("description")),
                            pattern,
                            '\\'
                    )
            );
        };
    }

    private static String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
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
                    effectivePrice(root, cb),
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
                    effectivePrice(root, cb),
                    maxPrice
            );
        };
    }

    private static Expression<BigDecimal> effectivePrice(
            Root<Tour> root,
            CriteriaBuilder cb
    ) {
        return cb.coalesce(
                root.get("discountPrice"),
                root.get("price")
        );
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

    private static Specification<Tour> sort(String sort) {
        return (root, query, cb) -> {

            if (sort == null || sort.isBlank()) {
                return null;
            }

            // Tránh áp ORDER BY vào count query của pagination
            if (Long.class.equals(query.getResultType())
                    || long.class.equals(query.getResultType())) {
                return null;
            }

            switch (sort) {
                case "price_asc" ->
                        query.orderBy(
                                cb.asc(
                                        effectivePrice(root, cb)
                                )
                        );

                case "price_desc" ->
                        query.orderBy(
                                cb.desc(
                                        effectivePrice(root, cb)
                                )
                        );

                case "rating" ->
                        query.orderBy(
                                cb.desc(root.get("rating")),
                                cb.desc(root.get("reviewCount"))
                        );

                case "newest" ->
                        query.orderBy(
                                cb.desc(root.get("createdAt"))
                        );

                default ->
                        throw new BadRequestException(
                                "INVALID_SORT",
                                "Unsupported sort: " + sort
                        );
            }

            return null;
        };
    }
}
