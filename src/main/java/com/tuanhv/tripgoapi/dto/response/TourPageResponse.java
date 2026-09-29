package com.tuanhv.tripgoapi.dto.response;

import java.util.List;

public record TourPageResponse(
        List<TourResponse> data,
        long total,
        int page,
        int size
) {
}
