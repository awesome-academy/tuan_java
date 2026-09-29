package com.tuanhv.tripgoapi.mapper;

import com.tuanhv.tripgoapi.dto.response.BookingDetailResponse;
import com.tuanhv.tripgoapi.dto.response.BookingResponse;
import com.tuanhv.tripgoapi.dto.response.BookingSummaryResponse;
import com.tuanhv.tripgoapi.dto.response.TourSummaryResponse;
import com.tuanhv.tripgoapi.entity.Booking;
import com.tuanhv.tripgoapi.entity.Tour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(
            target = "departureId",
            source = "tourDeparture.id"
    )
    @Mapping(
            target = "tourSlug",
            source = "tourDeparture.tour.slug"
    )
    @Mapping(
            target = "tourTitle",
            source = "tourDeparture.tour.title"
    )
    @Mapping(
            target = "date",
            source = "tourDeparture.startDate"
    )
    @Mapping(
            target = "contact.fullName",
            source = "fullName"
    )
    @Mapping(
            target = "contact.email",
            source = "email"
    )
    @Mapping(
            target = "contact.phone",
            source = "phone"
    )
    BookingResponse toResponse(Booking booking);

    @Mapping(
            target = "tour",
            source = "tourDeparture.tour"
    )
    @Mapping(
            target = "date",
            source = "tourDeparture.startDate"
    )
    BookingSummaryResponse toSummaryResponse(Booking booking);

    List<BookingSummaryResponse> toSummaryResponseList(List<Booking> bookings);

    TourSummaryResponse toTourSummaryResponse(Tour tour);

    @Mapping(
            target = "tour",
            source = "tourDeparture.tour"
    )
    @Mapping(
            target = "departureId",
            source = "tourDeparture.id"
    )
    @Mapping(
            target = "date",
            source = "tourDeparture.startDate"
    )
    @Mapping(
            target = "contact.fullName",
            source = "fullName"
    )
    @Mapping(
            target = "contact.email",
            source = "email"
    )
    @Mapping(
            target = "contact.phone",
            source = "phone"
    )
    BookingDetailResponse toDetailResponse(Booking booking);
}
