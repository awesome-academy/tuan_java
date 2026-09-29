package com.tuanhv.tripgoapi.controller.admin;

import com.tuanhv.tripgoapi.dto.admin.AdminBookingListItem;
import com.tuanhv.tripgoapi.entity.Booking;
import com.tuanhv.tripgoapi.enums.BookingStatus;
import com.tuanhv.tripgoapi.exception.BadRequestException;
import com.tuanhv.tripgoapi.exception.ConflictException;
import com.tuanhv.tripgoapi.service.admin.AdminBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/bookings")
@RequiredArgsConstructor
public class AdminBookingController {

    private final AdminBookingService adminBookingService;

    @GetMapping
    public String list(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model
    ) {
        Page<AdminBookingListItem> result = adminBookingService.search(status, page, size);
        model.addAttribute("result", result);
        model.addAttribute("status", status);

        return "admin/bookings/list";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status,
            RedirectAttributes redirectAttributes
    ) {
        try {
            adminBookingService.updateStatus(id, status);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Cập nhật trạng thái đơn thành công"
            );

        } catch (ConflictException | BadRequestException ex) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    ex.getMessage()
            );
        }

        return "redirect:/admin/bookings";
    }

}
