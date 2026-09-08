package com.tuanhv.tripgoapi.mapper;

import com.tuanhv.tripgoapi.dto.response.TourDepartureResponse;
import com.tuanhv.tripgoapi.entity.TourDeparture;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TourDepartureMapper {

    TourDepartureResponse toResponse(TourDeparture departure);
}
