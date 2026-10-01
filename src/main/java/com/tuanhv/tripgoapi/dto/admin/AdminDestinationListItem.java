package com.tuanhv.tripgoapi.dto.admin;

public record AdminDestinationListItem(
        Long id,
        String name,
        String slug,
        String imageUrl,
        long tourCount
) {
}
