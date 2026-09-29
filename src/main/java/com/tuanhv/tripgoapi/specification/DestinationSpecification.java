package com.tuanhv.tripgoapi.specification;

import com.tuanhv.tripgoapi.entity.Destination;
import org.springframework.data.jpa.domain.Specification;

public class DestinationSpecification {

    private DestinationSpecification() {
    }

    public static Specification<Destination> keyword(String keyword) {
        return (root, query, cb) -> {

            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            String escaped = escapeLike(keyword.trim().toLowerCase());
            String pattern = "%" + escaped + "%";

            return cb.or(
                    cb.like(
                            cb.lower(root.get("name")),
                            pattern,
                            '\\'
                    ),
                    cb.like(
                            cb.lower(root.get("slug")),
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
}
