package com.tuanhv.tripgoapi.service;

import com.tuanhv.tripgoapi.dto.request.CreateBookingRequest;
import com.tuanhv.tripgoapi.dto.response.BookingDetailResponse;
import com.tuanhv.tripgoapi.dto.response.BookingResponse;
import com.tuanhv.tripgoapi.dto.response.BookingSummaryResponse;

import java.util.List;

public interface BookingService {

    BookingResponse create(CreateBookingRequest request);

    List<BookingSummaryResponse> getMyBookings(String status);

    BookingDetailResponse getMyBooking(Long bookingId);

    BookingDetailResponse cancelBooking(Long bookingId);
}
