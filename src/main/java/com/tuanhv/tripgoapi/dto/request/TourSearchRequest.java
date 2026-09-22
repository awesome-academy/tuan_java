package com.tuanhv.tripgoapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TourSearchRequest {

    @Schema(
            description = "Tìm tour nổi bật",
            example = "true"
    )
    private Boolean featured;

    @Schema(
            description = "Từ khóa tìm kiếm",
            example = "Đà Nẵng"
    )
    private String q;

    @Schema(
            description = "Slug điểm đến",
            example = "da-nang"
    )
    private String destination;

    @Schema(
            description = "Slug loại hình tour",
            example = "beach"
    )
    private String category;

    @Schema(
            description = "Giá tối thiểu",
            example = "2000000"
    )
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "minPrice must be greater than or equal to 0"
    )
    private BigDecimal minPrice;

    @Schema(
            description = "Giá tối đa",
            example = "6000000"
    )
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "maxPrice must be greater than or equal to 0"
    )
    private BigDecimal maxPrice;

    @Schema(
            description = "Số ngày",
            example = "3"
    )
    @Min(
            value = 1,
            message = "duration must be greater than or equal to 1"
    )
    private Integer duration;

    @Schema(
            description = "Rating tối thiểu",
            example = "4.5"
    )
    @DecimalMin(
            value = "0.0",
            message = "rating must be greater than or equal to 0"
    )
    @DecimalMax(
            value = "5.0",
            message = "rating must be less than or equal to 5"
    )
    private BigDecimal rating;

    @Schema(
            description = """
                Kiểu sắp xếp:
                newest, price_asc, price_desc, rating
                """,
            example = "price_asc"
    )
    private String sort = "newest";

    @Schema(
            description = "Trang, bắt đầu từ 1",
            example = "1"
    )
    @Min(
            value = 1,
            message = "page must be greater than or equal to 1"
    )
    @NotNull
    private Integer page = 1;

    @Schema(
            description = "Số record mỗi trang",
            example = "12"
    )
    @Min(
            value = 1,
            message = "limit must be greater than or equal to 1"
    )
    @Max(
            value = 100,
            message = "limit must be less than or equal to 100"
    )
    @NotNull
    private Integer limit = 12;
}
