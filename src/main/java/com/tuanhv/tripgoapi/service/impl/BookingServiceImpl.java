package com.tuanhv.tripgoapi.service.impl;

import com.tuanhv.tripgoapi.dto.request.CreateBookingRequest;
import com.tuanhv.tripgoapi.dto.response.BookingDetailResponse;
import com.tuanhv.tripgoapi.dto.response.BookingResponse;
import com.tuanhv.tripgoapi.dto.response.BookingSummaryResponse;
import com.tuanhv.tripgoapi.entity.Booking;
import com.tuanhv.tripgoapi.entity.Tour;
import com.tuanhv.tripgoapi.entity.TourDeparture;
import com.tuanhv.tripgoapi.entity.User;
import com.tuanhv.tripgoapi.enums.BookingStatus;
import com.tuanhv.tripgoapi.exception.*;
import com.tuanhv.tripgoapi.generator.BookingCodeGenerator;
import com.tuanhv.tripgoapi.mapper.BookingMapper;
import com.tuanhv.tripgoapi.repository.BookingRepository;
import com.tuanhv.tripgoapi.repository.TourDepartureRepository;
import com.tuanhv.tripgoapi.repository.UserRepository;
import com.tuanhv.tripgoapi.security.CurrentUserProvider;
import com.tuanhv.tripgoapi.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final CurrentUserProvider currentUserProvider;
    private final TourDepartureRepository tourDepartureRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final BookingCodeGenerator bookingCodeGenerator;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public BookingResponse create(CreateBookingRequest request) {
        Long userId = currentUserProvider.getUserId();

        TourDeparture departure = tourDepartureRepository
                .findByIdForUpdate(request.departureId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NOT_FOUND",
                                "Không tìm thấy lịch khởi hành"
                        )
                );

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NOT_FOUND",
                                "Không tìm thấy người dùng"
                        )
                );

        Tour tour = departure.getTour();

        validateDepartureDate(departure);

        int requestedSeats = request.adults() + request.children();
        long bookedSeats = bookingRepository.sumBookedSeats(departure.getId());
        int availableSeats = tour.getMaxGroupSize() - Math.toIntExact(bookedSeats);

        if (requestedSeats > availableSeats) {
            throw new ConflictException(
                    "NOT_ENOUGH_SLOTS",
                    "Không còn đủ chỗ cho số lượng khách đã chọn"
            );
        }

        BigDecimal unitPrice = resolvePrice(departure);
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(requestedSeats));

        Booking booking = new Booking();
        booking.setCode(bookingCodeGenerator.generate());
        booking.setTourDeparture(departure);
        booking.setUser(user);
        booking.setAdults(request.adults());
        booking.setChildren(request.children());
        booking.setFullName(request.contact().fullName().trim());
        booking.setEmail(
                request.contact().email()
                        .trim()
                        .toLowerCase(Locale.ROOT)
        );
        booking.setPhone(request.contact().phone().trim());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(Instant.now());

        Booking saved = bookingRepository.save(booking);

        return bookingMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingSummaryResponse> getMyBookings(String status) {
        Long userId = currentUserProvider.getUserId();

        BookingStatus bookingStatus = parseStatus(status);

        List<Booking> bookings;

        if (bookingStatus == null) {
            bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
        } else {
            bookings = bookingRepository.findByUserIdAndStatusOrderByCreatedAtDesc(
                    userId,
                    bookingStatus
            );
        }

        return bookingMapper.toSummaryResponseList(bookings);
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDetailResponse getMyBooking(Long bookingId) {
        Long userId = currentUserProvider.getUserId();

        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NOT_FOUND",
                                "Không tìm thấy đơn đặt tour"
                        )
                );

        return bookingMapper.toDetailResponse(booking);
    }

    @Transactional
    @Override
    public BookingDetailResponse cancelBooking(Long bookingId) {
        Long userId = currentUserProvider.getUserId();

        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NOT_FOUND",
                                "Không tìm thấy đơn đặt tour"
                        )
                );

        if (!booking.getUser().getId().equals(userId)) {
            throw new ForbiddenException(
                    "FORBIDDEN",
                    "Bạn không có quyền hủy đơn này"
            );
        }

        validateCancelable(booking);

        booking.setStatus(BookingStatus.CANCELLED);

        return bookingMapper.toDetailResponse(booking);
    }

    private void validateDepartureDate(TourDeparture departure) {
        if (!departure.getStartDate().isAfter(LocalDate.now())) {
            throw new BadRequestException(
                    "INVALID_DEPARTURE_DATE",
                    "Ngày khởi hành không hợp lệ"
            );
        }
    }

    private BigDecimal resolvePrice(TourDeparture departure) {
        if (departure.getPrice() != null) {
            return departure.getPrice();
        }

        Tour tour = departure.getTour();

        if (tour.getDiscountPrice() != null) {
            return tour.getDiscountPrice();
        }

        if (tour.getPrice() != null) {
            return tour.getPrice();
        }

        throw new IllegalStateException(
                "No price configured for tour id=" + tour.getId()
        );
    }

    private BookingStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return BookingStatus.valueOf(
                    status.trim().toUpperCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException(
                    "INVALID_STATUS",
                    "Trạng thái booking không hợp lệ"
            );
        }
    }

    private void validateCancelable(Booking booking) {
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ConflictException(
                    "BOOKING_ALREADY_CANCELLED",
                    "Đơn đặt tour đã được hủy"
            );
        }

        if (!booking.getTourDeparture().getStartDate().isAfter(LocalDate.now())) {
            throw new ConflictException(
                    "BOOKING_CANNOT_BE_CANCELLED",
                    "Không thể hủy tour đã hoặc đang khởi hành"
            );
        }
    }
}
