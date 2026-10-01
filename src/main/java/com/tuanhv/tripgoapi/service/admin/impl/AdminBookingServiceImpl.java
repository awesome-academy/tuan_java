package com.tuanhv.tripgoapi.service.admin.impl;

import com.tuanhv.tripgoapi.dto.admin.AdminBookingListItem;
import com.tuanhv.tripgoapi.entity.Booking;
import com.tuanhv.tripgoapi.enums.BookingStatus;
import com.tuanhv.tripgoapi.exception.BadRequestException;
import com.tuanhv.tripgoapi.exception.ConflictException;
import com.tuanhv.tripgoapi.exception.ResourceNotFoundException;
import com.tuanhv.tripgoapi.repository.BookingRepository;
import com.tuanhv.tripgoapi.service.admin.AdminBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBookingServiceImpl implements AdminBookingService {

    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AdminBookingListItem> search(BookingStatus status, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 100);
        Pageable pageable = PageRequest.of(safePage - 1, safeSize);

        Page<Booking> bookings;

        if (status == null) {
            bookings = bookingRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            bookings = bookingRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        }

        return bookings.map(this::toListItem);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, BookingStatus target) {
        Booking booking = bookingRepository.findByIdForAdminUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NOT_FOUND",
                                "Không tìm thấy đơn đặt"
                        )
                );
        validateStatusTransition(booking, target);
        booking.setStatus(target);
    }

    private AdminBookingListItem toListItem(Booking booking) {
        int adults = booking.getAdults();
        int children = booking.getChildren();
        return new AdminBookingListItem(
                booking.getId(),
                booking.getCode(),
                booking.getFullName(),
                booking.getEmail(),
                booking.getTourDeparture().getTour().getTitle(),
                booking.getTourDeparture().getTour().getSlug(),
                booking.getTourDeparture().getStartDate(),
                adults,
                children,
                adults + children,
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getCreatedAt()
        );
    }

    private void validateStatusTransition(
            Booking booking,
            BookingStatus targetStatus
    ) {

        if (targetStatus == null) {
            throw new BadRequestException(
                    "INVALID_STATUS",
                    "Trạng thái không hợp lệ"
            );
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ConflictException(
                    "INVALID_STATUS_TRANSITION",
                    "Chỉ đơn PENDING mới có thể thay đổi trạng thái"
            );
        }

        if (targetStatus != BookingStatus.CONFIRMED && targetStatus != BookingStatus.CANCELLED) {
            throw new BadRequestException(
                    "INVALID_STATUS",
                    "Trạng thái đích không hợp lệ"
            );
        }
    }
}
