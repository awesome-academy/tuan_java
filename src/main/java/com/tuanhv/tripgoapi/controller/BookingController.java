package com.tuanhv.tripgoapi.controller;

import com.tuanhv.tripgoapi.dto.request.CreateBookingRequest;
import com.tuanhv.tripgoapi.dto.response.BookingDetailResponse;
import com.tuanhv.tripgoapi.dto.response.BookingResponse;
import com.tuanhv.tripgoapi.dto.response.BookingSummaryResponse;
import com.tuanhv.tripgoapi.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "05. Bookings")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Đặt tour",
            description = """
                Tạo booking cho user hiện tại.
        
                Backend lấy userId từ JWT và tự tính totalPrice.
                Client không được phép quyết định userId hoặc totalPrice.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Đặt tour thành công"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa đăng nhập hoặc JWT không hợp lệ"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy departure"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Không đủ chỗ"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Dữ liệu booking không hợp lệ"
            )
    })
    public BookingResponse createBooking(
            @Valid @RequestBody CreateBookingRequest request
    ) {
        return bookingService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Danh sách booking của tôi",
            description = """
                Chỉ trả booking của user hiện tại lấy từ JWT.
                Có thể lọc theo trạng thái.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Danh sách booking"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa xác thực"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "status không hợp lệ"
            )
    })
    public List<BookingSummaryResponse> getMyBookings(

            @Parameter(
                    description = """
                        Trạng thái:
                        pending, confirmed, cancelled
                        """,
                    example = "pending"
            )
            @RequestParam(required = false) String status
    ) {
        return bookingService.getMyBookings(status);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Chi tiết booking của tôi"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Chi tiết booking"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa xác thực"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Booking không tồn tại hoặc không thuộc user hiện tại"
            )
    })
    public BookingDetailResponse getBooking(
            @Parameter(
                    description = "Booking ID",
                    example = "123"
            )
            @PathVariable Long id
    ) {
        return bookingService.getMyBooking(id);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(
            summary = "Hủy booking"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Hủy thành công"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa xác thực"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Booking thuộc user khác"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy booking"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Booking không thể hủy"
            )
    })
    public BookingDetailResponse cancelBooking(
            @Parameter(
                    description = "Booking ID",
                    example = "123"
            )
            @PathVariable Long id
    ) {
        return bookingService.cancelBooking(id);
    }
}
