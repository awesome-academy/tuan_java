package com.tuanhv.tripgoapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(
        description = "Yêu cầu tạo booking"
)
public record CreateBookingRequest(

        @Schema(
                description = "ID lịch khởi hành",
                example = "101"
        )
        @NotNull(message = "departureId là bắt buộc")
        Long departureId,

        @Schema(
                description = "Số người lớn",
                example = "2"
        )
        @NotNull(message = "adults là bắt buộc")
        @Min(value = 1, message = "adults phải lớn hơn hoặc bằng 1")
        Integer adults,

        @Schema(
                description = "Số trẻ em",
                example = "1"
        )
        @NotNull(message = "children là bắt buộc")
        @Min(value = 0, message = "children phải lớn hơn hoặc bằng 0")
        Integer children,

        @NotNull(message = "contact là bắt buộc")
        @Valid
        ContactRequest contact
) {
}
