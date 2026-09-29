package com.tuanhv.tripgoapi.mapper;

import com.tuanhv.tripgoapi.dto.response.TourDetailResponse;
import com.tuanhv.tripgoapi.dto.response.TourResponse;
import com.tuanhv.tripgoapi.entity.Tour;
import com.tuanhv.tripgoapi.entity.TourDeparture;
import com.tuanhv.tripgoapi.entity.TourItinerary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                TourItineraryMapper.class,
                TourDepartureMapper.class
        }
)
public interface TourMapper {

    @Mapping(target = "destination", source = "destination.name")
    @Mapping(target = "category", source = "category.slug")
    TourResponse toResponse(Tour tour);

    @Mapping(target = "destination", source = "tour.destination.name")
    @Mapping(target = "category", source = "tour.category.slug")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "itineraries", source = "itineraries")
    @Mapping(target = "departures", source = "departures")
    TourDetailResponse toDetailResponse(
            Tour tour,
            List<String> images,
            List<TourItinerary> itineraries,
            List<TourDeparture> departures
    );
}
