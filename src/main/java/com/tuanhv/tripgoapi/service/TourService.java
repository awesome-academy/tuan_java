package com.tuanhv.tripgoapi.service;

import com.tuanhv.tripgoapi.dto.request.TourSearchRequest;
import com.tuanhv.tripgoapi.dto.response.TourAvailabilityResponse;
import com.tuanhv.tripgoapi.dto.response.TourDetailResponse;
import com.tuanhv.tripgoapi.dto.response.TourPageResponse;

import java.util.List;

public interface TourService {

    TourPageResponse searchTours(TourSearchRequest request);

    TourDetailResponse getTourDetail(String slug);

    List<TourAvailabilityResponse> getTourAvailability(String slug, String month);
}
