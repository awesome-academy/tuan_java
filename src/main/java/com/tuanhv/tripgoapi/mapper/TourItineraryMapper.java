package com.tuanhv.tripgoapi.mapper;

import com.tuanhv.tripgoapi.dto.response.TourItineraryResponse;
import com.tuanhv.tripgoapi.entity.TourItinerary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TourItineraryMapper {

    TourItineraryResponse toResponse(TourItinerary itinerary);
}
