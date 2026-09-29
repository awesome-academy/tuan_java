package com.tuanhv.tripgoapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Response lỗi chuẩn"
)
public record ErrorResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        ErrorDetail error
) {
    public record ErrorDetail(
            @Schema(
                    example = "NOT_FOUND"
            )
            String code,

            @Schema(
                    example = "Không tìm thấy"
            )
            String message
    ) {
    }
}
