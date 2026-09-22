package com.tuanhv.tripgoapi.controller;

import com.tuanhv.tripgoapi.dto.request.TourSearchRequest;
import com.tuanhv.tripgoapi.dto.response.TourAvailabilityResponse;
import com.tuanhv.tripgoapi.dto.response.TourDetailResponse;
import com.tuanhv.tripgoapi.dto.response.TourPageResponse;
import com.tuanhv.tripgoapi.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
@Tag(name = "04. Tours")
public class TourController {

    private final TourService tourService;

    @GetMapping
    @Operation(
            summary = "Tìm kiếm và lọc tour",
            description = """
                Hỗ trợ tìm kiếm, lọc, sắp xếp và phân trang tour.
                page bắt đầu từ 1.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Danh sách tour"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Query parameter không hợp lệ"
            )
    })
    public TourPageResponse getTours(
            @Valid @ModelAttribute TourSearchRequest request
    ) {
        return tourService.searchTours(request);
    }

    @GetMapping("/{slug}")
    @Operation(
            summary = "Chi tiết tour",
            description = """
                Trả thông tin đầy đủ của tour:
                ảnh, lịch trình, ngày khởi hành và thông tin cơ bản.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tìm thấy tour"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy tour"
            )
    })
    public TourDetailResponse getTourDetail(
            @Parameter(
                    description = "Slug của tour",
                    example = "da-nang-bien-my-khe-3n2d"
            )
            @PathVariable
            String slug
    ) {
        return tourService.getTourDetail(slug);
    }

    @GetMapping("/{slug}/availability")
    @Operation(
            summary = "Kiểm tra lịch khởi hành và số chỗ",
            description = """
                Truyền month theo định dạng yyyy-MM,
                chỉ trả departure trong tháng đó
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Danh sách lịch khởi hành"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "month không đúng định dạng"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy tour"
            )
    })
    public List<TourAvailabilityResponse> getAvailability(
            @Parameter(
                    description = "Slug của tour",
                    example = "da-nang-bien-my-khe-3n2d"
            )
            @PathVariable
            String slug,

            @Parameter(
                    description = "Tháng cần kiểm tra, định dạng yyyy-MM",
                    example = "2026-07"
            )
            @RequestParam
            String month
    ) {
        return tourService.getTourAvailability(slug, month);
    }
}
