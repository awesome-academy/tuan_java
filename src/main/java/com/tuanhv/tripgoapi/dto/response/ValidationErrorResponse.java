package com.tuanhv.tripgoapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record ValidationErrorResponse(
        ValidationError error
) {
    public record ValidationError(
            String code,
            String message,
            @Schema(
                    example = """
                        {
                          "email": "Email không hợp lệ",
                          "password": "Mật khẩu phải từ 8 ký tự"
                        }
                        """
            )
            Map<String, String> fields
    ) {
    }
}
