package com.tuanhv.tripgoapi.service.admin.impl;

import com.tuanhv.tripgoapi.dto.admin.AdminDashboardView;
import com.tuanhv.tripgoapi.enums.BookingStatus;
import com.tuanhv.tripgoapi.repository.BookingRepository;
import com.tuanhv.tripgoapi.repository.TourRepository;
import com.tuanhv.tripgoapi.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final TourRepository tourRepository;
    private final BookingRepository bookingRepository;

    @Override
    public AdminDashboardView getDashboard() {
        return new AdminDashboardView(
                tourRepository.count(),
                bookingRepository.count(),
                bookingRepository.countByStatus(BookingStatus.PENDING),
                bookingRepository.countByStatus(BookingStatus.CONFIRMED),
                bookingRepository.sumConfirmedRevenue()
        );
    }
}
