package com.tuanhv.tripgoapi.dto.admin;

import java.util.List;

public record AdminTourFormOptions(
        List<DestinationOption> destinations,
        List<CategoryOption> categories
) {

    public AdminTourFormOptions {
        destinations = List.copyOf(destinations);
        categories = List.copyOf(categories);
    }

    public record DestinationOption(
            Long id,
            String name
    ) {
    }

    public record CategoryOption(
            Long id,
            String name
    ) {
    }
}