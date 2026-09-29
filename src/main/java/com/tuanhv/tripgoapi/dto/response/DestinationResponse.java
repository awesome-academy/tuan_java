package com.tuanhv.tripgoapi.dto.response;

public record DestinationResponse(
        Long id,
        String slug,
        String name,
        String image,
        Long tourCount
) { }
