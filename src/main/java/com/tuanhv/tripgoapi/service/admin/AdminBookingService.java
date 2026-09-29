package com.tuanhv.tripgoapi.service.admin;

import com.tuanhv.tripgoapi.dto.admin.AdminBookingListItem;
import com.tuanhv.tripgoapi.enums.BookingStatus;
import org.springframework.data.domain.Page;

public interface AdminBookingService {

    Page<AdminBookingListItem> search(BookingStatus status, int page, int size);

    void updateStatus(Long id, BookingStatus target);
}
